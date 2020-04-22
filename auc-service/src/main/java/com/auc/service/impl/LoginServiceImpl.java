package com.auc.service.impl;

import com.auc.common.AppException;
import com.auc.common.constants.AucConst;
import com.auc.common.enums.ClientEnum;
import com.auc.common.enums.DataSourceEnum;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.enums.LoginTypeEnum;
import com.auc.common.enums.RedisKey;
import com.auc.common.enums.SMSEnum;
import com.auc.common.utils.CommonsUtils;
import com.auc.common.utils.ConsistentHashRouter;
import com.auc.context.LoginStrategyContext;
import com.auc.dao.Role;
import com.auc.dao.User;
import com.auc.domain.dto.requestdto.LoginBaseRequestDTO;
import com.auc.domain.dto.requestdto.MPLoginRequestDTO;
import com.auc.domain.dto.requestdto.ProxyReqRequestDTO;
import com.auc.domain.dto.requestdto.SmsCodeRequestDTO;
import com.auc.domain.dto.requestdto.UserDomainLoginRequestDTO;
import com.auc.domain.dto.requestdto.UserSystemLoginRequestDTO;
import com.auc.domain.dto.responsedto.DomainLoginInfoResponseDTO;
import com.auc.domain.dto.responsedto.LoginInfoResponseDTO;
import com.auc.domain.dto.responsedto.MpLoginInfoResponseDTO;
import com.auc.domain.dto.responsedto.SystemLoginInfoResponseDTO;
import com.auc.dubbo.user.dao.MergeUser;
import com.auc.service.IAccountConvertService;
import com.auc.service.IOperationalService;
import com.auc.service.IRoleService;
import com.auc.service.IUserService;
import com.auc.service.LoginService;
import com.auc.service.RedisService;

import com.auc.utils.PasswordUtils;
import com.google.common.collect.Lists;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.Base64;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

  @Autowired
  private Environment environment;

  @Autowired
  private RedisService redisService;

  @Autowired
  private SmsMessageServiceImpl smsMessageService;

  @Autowired
  private IRoleService roleService;

  @Autowired
  public IAccountConvertService accountConvertService;

  @Autowired
  public LoginStrategyContext loginStrategyContext;

  @Autowired
  private IUserService userService;
  @Autowired
  IOperationalService operationalService;

  @Value("${login.pw.error.maxnum}")
  private int pwMaxErrorNum;
  //验证失败后的redis留存时间
  @Value("${login.pw.error.interval}")
  private int pwMaxErrorInterval;

  @Autowired
  private ConsistentHashRouter<String> consistentHashRouter;

  @Override
  public DomainLoginInfoResponseDTO domainLogin(UserDomainLoginRequestDTO dto) {
    this.req2Domain(dto);
    User user =userService.getByUserNameAndDomainCode(dto.getUserName(), dto.getDomainCode().getCode());
    if (user == null) {
      throw new AppException(ErrorCodeEnum.USER_NOT_EXISTS);
    }
    //异步获取权限
    CompletableFuture<List<String>> cf = this.getAuthorities(user.getId());
    //扩展处理
    user = this.ext(user,dto);
    return new DomainLoginInfoResponseDTO(user, this.getAuthorities(cf),dto.getDomainCode().getCode());
  }

  /**
   * 获取权限和角色
   * @param userId
   * @return
   */
  public CompletableFuture<List<String>> getAuthorities(Integer userId){
    CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(()->{
      List<String> roles = roleService.getRoleInfoByUserId(userId).stream().map(Role::getRoleCode).collect(Collectors
          .toList());
      List<String> authorities = operationalService.getOperationalByRole(roles);
      authorities.addAll(roles.stream().map(r -> "ROLE_" + r).collect(Collectors.toList()));
      return authorities;
    });
    return future;
  }

  /**
   * 阻塞获取结果
   * @param cf
   * @return
   */
  public List<String> getAuthorities(CompletableFuture<List<String>> cf){
    try {
      return cf.get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return Lists.newArrayList();
  }


  @Override
  public LoginInfoResponseDTO appAccountLogin(UserSystemLoginRequestDTO dto) {
    User user = this.req2AppAccount(dto);
    if (user == null) {
      throw new AppException(ErrorCodeEnum.USER_NOT_EXISTS);
    }
    //异步获取权限
    CompletableFuture<List<String>> cf = this.getAuthorities(user.getId());
    //扩展处理
    user = this.ext(user,dto);
    return new SystemLoginInfoResponseDTO(user, this.getAuthorities(cf));
  }

  private <T extends LoginBaseRequestDTO> User ext(User user,T dto){
    //非AUC WEB登录,转换账号
    if(!ClientEnum.AUC.getCode().equals(dto.getClient_id())){
      user = this.commonConvertUserName(user, dto.getClient_id());
    }
    //自定义客户端登录策略处理-异步处理
    loginStrategyContext.loginStrategy(dto,user,dto.getClient_id());
    return user;
  }

  /**
   * 应用系统账号登录
   * @param dto
   * @return
   */
  private User req2AppAccount(UserSystemLoginRequestDTO dto) {
    // 登录错误次数限制
    String redisPwKey = AucConst.PW_ERROR_REDIS_PATH.
        concat(RedisKey.PW_ERROR_NUM.name()).
        concat("_").
        concat(dto.getUserName());
    Integer pwError = redisService.get(redisPwKey,Integer.class);
    if( pwError !=null && pwError.intValue() >= pwMaxErrorNum ){ //判断依据和lua保持一致,都是大于等于
      throw new AppException(ErrorCodeEnum.PW_ERROR_TOO_MUCH,
          "密码错误次数过多,请" + redisService.getExpire(redisPwKey) + "秒后再试");
    };
    List<User> ul = userService.checkAppAccount(dto.getUserName(), null, dto.getSourceCode());
    if (ul == null || ul.isEmpty()) {
      redisService.pwErrorLimitLua(RedisKey.PW_ERROR_NUM,dto.getUserName(),pwMaxErrorNum,pwMaxErrorInterval);
      throw new AppException(ErrorCodeEnum.UNEXCEPTED, "账号或密码错误");
    }
    if (ul.size() > 1) {
      throw new AppException(ErrorCodeEnum.UNSPECIFIED_DATA_SOURCE);
    }
    try {
      if (!ul.get(0).getPassword().equals(PasswordUtils
          .getMd5Hash(new String(Base64.getDecoder().decode(dto.getPassword())),
              ul.get(0).getSalt()))) {
        redisService.pwErrorLimitLua(RedisKey.PW_ERROR_NUM,dto.getUserName(),pwMaxErrorNum,pwMaxErrorInterval);
        throw new AppException(ErrorCodeEnum.UNEXCEPTED, "账号或密码错误");
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    ul.get(0).setSourceCode(dto.getSourceCode());//设置sourceCode
    return ul.get(0);
  }

  /**
   * 转换账号
   * @param user
   * @param clientId
   * @return
   */
  private User commonConvertUserName(User user,String clientId){
    MergeUser mu = new MergeUser();
    mu.setUserName(user.getUserName());
    mu.setSourceCode(user.getSourceCode());
    mu = accountConvertService.convertUserName(mu,String.valueOf(user.getId()),clientId);
    if(mu.getUserName().equals(user.getUserName())
        && mu.getSourceCode().equals(user.getSourceCode())){
      return user;
    }
    //转换新用户信息
    return userService.getByUsernameAndSourceCode(mu.getUserName(), mu.getSourceCode());
  }

  /**
   * 请求域服务验证
   * @param dto
   * @return
   */
  private void req2Domain(UserDomainLoginRequestDTO dto){
    //电器的负载均衡，控股暂时不管
    String address = null;
    String domainCode = dto.getDomainCode().getCode().toLowerCase();
    if(domainCode.equals("gomedq")){
      address = consistentHashRouter.routeNode(dto.getUserName()+ LocalTime.now());
    }else{
      address = environment.getProperty("domain." + domainCode + ".address");
    }
    String domain = environment.getProperty("domain." + domainCode + ".suffix");
    if (StringUtils.isEmpty(address) || StringUtils.isEmpty(domain)) { // IP地址和域后缀不能为空
      throw new AppException(ErrorCodeEnum.ILLEGAL_PARAMETER, "IP地址和域后缀不能为空，请检查相关配置");
    }
    Hashtable<String, String> env = new Hashtable<>();
    env.put(Context.SECURITY_AUTHENTICATION, "simple");// LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
    env.put(Context.SECURITY_PRINCIPAL, dto.getUserName().indexOf(domain) > 0 ? dto.getUserName() : dto.getUserName() + "@" + domain); // 用户名
    env.put(Context.SECURITY_CREDENTIALS, new String(Base64.getDecoder().decode(dto.getPassword())));// 密码
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
    env.put(Context.PROVIDER_URL, "ldap://" + address + ":" + "389");// Url
    env.put("com.sun.jndi.ldap.read.timeout",
        environment.getProperty("domain." + domainCode + ".timeout.read"));//设置读取超时时间
    env.put("com.sun.jndi.ldap.connect.timeout",
        environment.getProperty("domain." + domainCode + ".timeout.connect"));//设置连接超时时间
    DirContext ctx = null;
    // 登录错误次数限制
    String redisPwKey = AucConst.PW_ERROR_REDIS_PATH.
        concat(RedisKey.PW_ERROR_NUM.name()).
        concat("_").
        concat(env.get(Context.SECURITY_PRINCIPAL));
    Integer pwError = redisService.get(redisPwKey,Integer.class);
    if( pwError !=null && pwError.intValue() >= pwMaxErrorNum ){ //判断依据和lua保持一致,都是大于等于
      throw new AppException(ErrorCodeEnum.PW_ERROR_TOO_MUCH,
          "密码错误次数过多,请" + redisService.getExpire(redisPwKey) + "秒后再试");
    };
    try {
      ctx = new InitialDirContext(env);
    } catch (NamingException e) {
      log.error("domain login naming exception : {}", e);
      if(e.getRootCause() instanceof java.net.SocketTimeoutException){
        throw new AppException(ErrorCodeEnum.DOMAIN_SERVER_ERROR);
      }
      boolean isExpired = userService.isExpiredOfPassword(dto.getUserName(),
          domainCode.equals("gomedq") ? "gome" : "gis");
      if(isExpired){
        throw new AppException(ErrorCodeEnum.UNEXCEPTED, "域密码过期");
      }
      throw new AppException(ErrorCodeEnum.UNEXCEPTED, "账号或密码错误");
    } finally {
      if (ctx != null) {
        //暂时不删除,等时间自然过期
        //redisService.asyncDelete(redisPwKey);
        try {
          ctx.close();
        } catch (NamingException e) {
          e.printStackTrace();
        }
      }else{//用户名密码错误
        redisService.pwErrorLimitLua(RedisKey.PW_ERROR_NUM,env.get(Context.SECURITY_PRINCIPAL),pwMaxErrorNum,pwMaxErrorInterval);
      }
    }
  }

  /**
   * 接口代理模式,不创建token,只做验证
   * @param dto
   * @return
   */
  public <T extends LoginInfoResponseDTO> T proxyLogin(ProxyReqRequestDTO dto){
    switch (dto.getLoginTypeEnum()){
      case DOMAIN:
        UserDomainLoginRequestDTO udr = new UserDomainLoginRequestDTO();
        BeanUtils.copyProperties(dto,udr);
        if(dto.isCreateToken()){
          return (T) this.domainLogin(udr);
        }else{
          this.req2Domain(udr);
        }
        break;
      case SYSTEM:
        UserSystemLoginRequestDTO usr = new UserSystemLoginRequestDTO();
        BeanUtils.copyProperties(dto,usr);
        if(dto.isCreateToken()){
          return (T) this.appAccountLogin(usr);
        }else{
          this.req2AppAccount(usr);
        }
        break;
      case MOBILE:
        MPLoginRequestDTO mrd = new MPLoginRequestDTO();
        BeanUtils.copyProperties(dto,mrd);
        if(dto.isCreateToken()){
          return (T) this.mpLogin(mrd);
        }else{
          this.mpLogin(mrd);
        }
      default:
          new AppException(ErrorCodeEnum.ILLEGAL_PARAMETER);
    }
    return null;
  }

  @Override
  public MpLoginInfoResponseDTO mpLogin(MPLoginRequestDTO dto) {
    // 获取redis缓存的验证码，如果没有则说明验证码已过期，返回错误信息
    String smsCode = redisService.get(RedisKey.LOGIN_SMS_CODE, dto.getUserName());
    if (StringUtils.isEmpty(smsCode) || !dto.getPassword().equals(smsCode)) {
      return null;
    }
    User user = userService.getByMobilePhoneAndSourceCode(dto.getUserName(),
        StringUtils.isEmpty(dto.getSourceCode()) ? DataSourceEnum.DQ.getSourceCode() : dto.getSourceCode());
    if (user == null) {
      throw new AppException(ErrorCodeEnum.USER_NOT_EXISTS);
    }
    //异步获取权限
    CompletableFuture<List<String>> cf = this.getAuthorities(user.getId());
    //扩展处理
    user = this.ext(user,dto);
    return new MpLoginInfoResponseDTO(user, this.getAuthorities(cf));
  }

  @Override
  public boolean inValid(String userId) {
    String token = redisService.get(AucConst.JWT_USER_REDIS_PATH + userId);
    if(!StringUtils.isEmpty(token)){
      // 删除 token 密钥对应缓存
      redisService.delete(AucConst.JWT_SECRET_REDIS_PATH + token);
    }
    // 删除用户id token缓存
    redisService.delete(AucConst.JWT_USER_REDIS_PATH + userId);
    return true;
  }

  @Override
  public boolean sendSMSCode(SmsCodeRequestDTO dto) {
    // 获取前台参数
    String mobilePhone = dto.getMobilePhone();
    // redis限流手机号码，单位时间1小时，只能发送5次
    if(!redisService.rangeLimitLua(RedisKey.SMS_FREQUENCY,mobilePhone,5,3600)){
      throw new AppException(ErrorCodeEnum.SMS_FREQUENCY_IS_HIGH);
    }

    // TODO 需要布隆过滤/用户查询,此用户是否是我们系统用户,非我们系统用户，不发验证码

    if (!userService.isMobileExist(mobilePhone)) {
      throw new AppException(ErrorCodeEnum.MOBILE_PHONE_IS_EMPTY, "手机号在系统中不存在");
    }
    // 检查redis是否已有验证码，如果有则直接使用，否则生成新的验证码
    String smsCode = redisService.get(RedisKey.SMS_CODE, mobilePhone);
    String expireCode =  dto.getSmsType().name()==SMSEnum.SMS_RESET_PWD.name()?"resetExpireMinutes":"loginExpireMinutes";
    RedisKey redisKey = dto.getSmsType().name()==SMSEnum.SMS_RESET_PWD.name()?RedisKey.RESET_SMS_CODE:RedisKey.LOGIN_SMS_CODE;
    if (org.apache.commons.lang3.StringUtils.isBlank(smsCode)) {
      smsCode = CommonsUtils.generateSMSCode();
      redisService.set(redisKey, mobilePhone, smsCode,
          Long.parseLong(environment.getProperty("message.sms.code."+expireCode)), TimeUnit.MINUTES);
    }
    Map<String, String> data = new HashMap<>();
    data.put("smsCode", smsCode);
    String content = smsMessageService.buildContent(dto.getSmsType().getContent(), data);
    return smsMessageService.send(dto.getMobilePhone(), content);
  }


  /**
   * @author  zhangchangzhong
   * @create  2019/12/24 11:20
   * @desc
   * 校验验证码修改密码
   **/
  @Override
  public void checkCode(String code,String mobilePhome) throws AppException{
    // 获取redis缓存的验证码，如果没有则说明验证码已过期，返回错误信息
    String smsCode = redisService.get(RedisKey.RESET_SMS_CODE, mobilePhome);
    if (StringUtils.isEmpty(smsCode) ) {
      throw new AppException(ErrorCodeEnum.PHONE_CODE_IS_WRONG,"验证码已过期,请重新发送验证码");
    }
    if(!code.equals(smsCode)){
      throw new AppException(ErrorCodeEnum.PHONE_CODE_IS_WRONG,"验证码错误,请重新输入验证码");
    }
  }
}
