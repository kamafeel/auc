package com.auc.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import com.auc.common.AppException;
import com.auc.common.constants.AucConst;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.enums.RedisKey;
import com.auc.dao.Datasource;
import com.auc.dao.Domain;
import com.auc.dao.User;
import com.auc.domain.dto.responsedto.UpdatePasswordResponseDTO;
import com.auc.dubbo.user.dao.MergeUser;
import com.auc.mapper.UserMapper;

import com.auc.service.*;

import com.auc.service.IDatasourceService;
import com.auc.service.IRoleService;
import com.auc.service.IUserService;

import com.auc.service.ICacheService;

import com.auc.domain.vo.UserRoleRelationVO;
import com.auc.utils.PasswordUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.swagger.models.auth.In;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import javax.xml.xpath.XPathConstants;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
  @Autowired
  public IDatasourceService datasourceService;
  @Autowired
  private IRoleService roleService;
  @Autowired
  private IDomainService iDomainService;

  @Autowired
  private ICacheService iCacheService;

@Autowired
private LoginService loginService;
  /**
   * 清空缓存
   */
  @CacheEvict(value="userCache",allEntries=true)
  @Override
  public void clearCache() {
  }

  @Value("${domain.gomehlw.hlwaddomain}")
  private String hlwAdDomain;

  @Value("${domain.gomedq.dqaddomain}")
  private String dqAdDomain;

  @Value("${domain.isExpired.url}")
  private String isExpiredUrl;

  @Override
  public void syncUserData(List<Map<String, Object>> syncData) {
    //先清空
    baseMapper.deleteUserSync();
    baseMapper.deleteAUCUserBackup();
    //插入中间表
    ListUtils.partition(syncData, 2000).forEach(is -> {
      baseMapper.batchInsertUserSync(is);
    });
    //备份AUC用户密码，状态信息
    baseMapper.backupAUCUser();
    //删除AUC信息
    baseMapper.deleteUserBySync();
    baseMapper.syncUser();
  }

  @Override
  public Collection<Integer> getMergeUserIdsWithYourself(Integer userId) {
    HashSet<Integer> mergeUserIds = Sets.newHashSet();
    //添加自己本身的userId
    mergeUserIds.add(userId);
    // 先查用户合并信息，如果有合并账号信息，则用合并账号的username和sourceCode到账号转换表查转换信息
    List<Map<String, Object>> mas = iCacheService.getAllMergeAccounts();
    Map<String, Object> mergeMap = mas.stream()
        .filter(m -> String.valueOf(m.get("userId")).equals(userId.toString()))
        .findAny().orElse(null);
    if (mergeMap == null) {
      return mergeUserIds;
    }
    mas.stream()
        .filter(
            m -> String.valueOf(m.get("mergeId")).equals(String.valueOf(mergeMap.get("mergeId"))))
        .forEach(m -> { mergeUserIds.add(Integer.valueOf(m.get("userId").toString()));});
    return mergeUserIds;
  }

  @Override
  public List<MergeUser> getMergeUsers(String userId){
    List<MergeUser> mergeAccounts = Lists.newArrayList();
    List<Map<String,Object>> mas = iCacheService.getAllMergeAccounts();
    Map<String,Object> mergeMap = mas.stream().filter(m-> String.valueOf(m.get("userId")).equals(userId))
        .findAny().orElse(null);
    if(mergeMap == null){
      return mergeAccounts;
    }
    mas.stream()
        .filter(m-> String.valueOf(m.get("mergeId")).equals(String.valueOf(mergeMap.get("mergeId"))))
        .forEach(m->{
          MergeUser mu = new MergeUser();
          mu.setUserName(String.valueOf(m.get("userName")));
          mu.setSourceCode(String.valueOf(m.get("sourceCode")));
          mergeAccounts.add(mu);
        });
    return mergeAccounts;
  }

  public UpdatePasswordResponseDTO updateAllPassword(Integer userId, String newPassword) throws AppException {
    UpdatePasswordResponseDTO responseDTO = new UpdatePasswordResponseDTO(true,"","");
    //所以修改结果的集合
    List<UpdatePasswordResponseDTO>  allDtoList = new ArrayList<>();
    Collection<Integer> userIds = this.getMergeUserIdsWithYourself(userId);
        if(userIds==null||userIds.size()<=0){
      throw new AppException(ErrorCodeEnum.UPDATEPASSWORD_USERERRO,"获取用户合并信息失败");
    }
    List<User>  userMergeList= getUsersByUids(userIds);
    if(null!=userIds&&userIds.size()>0){
      //校验新密码和旧密码是否相同
      //这里每个账号都循环避免首次登录账号不统一的情况
      for (User mergeUser:userMergeList) {
        this.comparePassword(mergeUser, newPassword);
      }
        for (User mergeUser:userMergeList) {

          //根据合并用户查询出的username和sourcecode查询对应的user对象  一般为一数据源对一个账号
          //需要应用系统账号(appcode)  不管合并用户和当前登录必须修改
          UpdatePasswordResponseDTO sysMsg = this.updateSystemPasswordBus( mergeUser, newPassword);
          allDtoList.add(sysMsg);
          //账号对应的域账号修改
          UpdatePasswordResponseDTO domainMsg = this.updateDomainPasswordBySourceCode(mergeUser,newPassword,mergeUser.getSourceCode());
          allDtoList.add(domainMsg);
        }
    }
    List<UpdatePasswordResponseDTO> filterErrorDtoList =  allDtoList.stream().filter(dto->dto.getStatus()==false).collect(Collectors.toList());
    List<UpdatePasswordResponseDTO>  filterSuccessDtoList =  allDtoList.stream().filter(dto->dto.getStatus()==true).collect(Collectors.toList());

    if(null!=filterSuccessDtoList&&filterSuccessDtoList.size()>0){
      responseDTO.setStatus(true);
      responseDTO.setErrorMsg("");
      StringBuilder successStringBuilder = new StringBuilder();
      filterSuccessDtoList.stream().forEach(
              dto->successStringBuilder.append(dto.getSuccessMsg())
      );
      responseDTO.setSuccessMsg(successStringBuilder.toString());
    }
    if(null!=filterErrorDtoList&&filterErrorDtoList.size()>0){
      StringBuilder errorStringBuilder = new StringBuilder();
      filterErrorDtoList.stream().forEach(
             dto->errorStringBuilder.append(dto.getErrorMsg())
      );
      responseDTO.setStatus(false);
      responseDTO.setErrorMsg(errorStringBuilder.toString());
    }
    return  responseDTO;
  }

  @Override
  public User getByUserNameAndDomainCode(String userName, String domainCode) {
    return baseMapper.getByUserNameAndDomainCode(userName, domainCode);
  }

  @Override
  public User getByUsernameAndSourceCode(String userName, String sourceCode) {
    return baseMapper.getByUsernameAndSourceCode(userName, sourceCode);
  }

  @Override
  public User getByMobilePhoneAndSourceCode(String mobilePhone, String sourceCode) {
    return baseMapper.getByMobilePhoneAndSourceCode(mobilePhone, sourceCode);
  }

  @Override
  public boolean isMobileExist(String mobilePhone) {
    return baseMapper.selectList(new QueryWrapper<User>().lambda()
        .eq(User::getMobilePhone, mobilePhone)).size() > 0;
  }

  @Override
  public List<Map<String, String>> getDataSourceByMobilePhone(String mobilePhone) {
    return baseMapper.getDataSourceByMobilePhone(mobilePhone);
  }

  @Override
  public List<Map<String, String>> getDataSourceByAppAccount(String appAccount) {
    return baseMapper.getDataSourceByAppAccount(appAccount);
  }


  public List<User> getUsersByUids(Collection<Integer> userIds) {
    return baseMapper.getUsersByUids(userIds);
  }

  /**
   * @author  zhangchangzhong
   * @create  2019/12/19 16:36
   * @desc
   * 根据域code修改对应的账号密码
   ** 支持互联网,电器域
   **/
  public UpdatePasswordResponseDTO updateDomainPasswordBySourceCode(User user,String newPassword,String sourceCode){
    UpdatePasswordResponseDTO updatePasswordResponseDTO = new UpdatePasswordResponseDTO();
    Boolean hlwBoolean = false;
    Boolean dqBoolean = false;
    //电器/互联网只需要根据数据源和账号修改密码
    if("HLW".equals(sourceCode)){
       hlwBoolean =  updateDomainPassword( user.getUserName(),  newPassword,  "gis", hlwAdDomain);
      updatePasswordResponseDTO.setStatus(hlwBoolean);
      if(hlwBoolean){
        updatePasswordResponseDTO.setSuccessMsg(user.getUserName()+"互联网域密码修改成功;");
      }else{
        updatePasswordResponseDTO.setErrorMsg(user.getUserName()+"互联网域密码修改失败;");
      }
    }else if ("DQ".equals(sourceCode)){
       dqBoolean =  updateDomainPassword( user.getUserName(),  newPassword,  "gome", dqAdDomain);
      updatePasswordResponseDTO.setStatus(dqBoolean);
      if(dqBoolean){
        updatePasswordResponseDTO.setSuccessMsg(user.getUserName()+"电器域密码修改成功;");
      }else{
        updatePasswordResponseDTO.setErrorMsg(user.getUserName()+"电器域密码修改失败;");
      }
    }else if ("KG".equals(sourceCode)){
      //暂不支持管理员修改密码
//      stringBuilder.append(user.getUserName()+" 控股域密码暂不支持修改");
      updatePasswordResponseDTO.setStatus(true);
//      updatePasswordResponseDTO.setErrorMsg(user.getUserName()+"请到原控股OA修改;");
      updatePasswordResponseDTO.setSuccessMsg(user.getUserName()+"请到原控股OA修改;");
    }else{
      //其他的数据源账号忽略掉
      updatePasswordResponseDTO.setStatus(true);
      updatePasswordResponseDTO.setSuccessMsg("");
    }
    return  updatePasswordResponseDTO;
  }

  /**
   * @author  zhangchangzhong
   * @create  2019/12/19 16:36
   * @desc
   * 修改应用账号密码
   **/
  public UpdatePasswordResponseDTO updateSystemPasswordBus(User user,String newPassword){
    UpdatePasswordResponseDTO updatePasswordResponseDTO = new UpdatePasswordResponseDTO();
    Boolean resBoolean =  this.updateAppcodePassword( user, newPassword);
    updatePasswordResponseDTO.setStatus(resBoolean);
    if(resBoolean){
//      updatePasswordResponseDTO.setSuccessMsg(user.getUserName()+" 系统账号密码修改成功");
      //业务要求:系统账号修改成功提示为空
      updatePasswordResponseDTO.setSuccessMsg("");
    }else{
      updatePasswordResponseDTO.setErrorMsg(user.getUserName()+"系统账号密码修改失败;");
    }
    //电器/互联网只需要根据数据源和账号修改密码
    return updatePasswordResponseDTO;
  }
  /**
   * @author  zhangchangzhong
   * @create  2019/12/17 16:31
   * @desc
   * 发送修改密码请求
   **/
  private Boolean updateDomainPassword(String adAccount, String adNewPassword, String adSource,String adDomain) {
    Boolean res = false;
    try {
      SoapClient client = SoapClient.create(adDomain)
              // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
              .setMethod("tem:SetAdUserPassword", "http://tempuri.org/")
              // 设置参数，此处自动添加方法的前缀：web
              .setParam("adSource", adSource)
              .setParam("adAccount", adAccount)
              .setParam("adPassword", adNewPassword);

      // 发送请求，参数true表示返回一个格式化后的XML内容
      // 返回内容为XML字符串，可以配合XmlUtil解析这个响应
      String s =client.send(true);
      Document resdoc = XmlUtil.parseXml (s);
      res= (Boolean) XmlUtil.getByXPath("//SetAdUserPasswordResponse/SetAdUserPasswordResult", resdoc, XPathConstants.BOOLEAN);
    }catch (Exception e){
      res = false;
    }
    return res;
  }
/**
 * @author  zhangchangzhong
 * @create  2019/12/18 17:49
 * @desc
 * 修改系统账号(appcode)密码
 * 数据操作层
 **/
  private  Boolean  updateAppcodePassword(User user,String newPassword){
    Boolean res =false;
    try {
      //把新密码加盐md5加密存储
     newPassword =   PasswordUtils.getMd5Hash(newPassword,user.getSalt());
      UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
      userUpdateWrapper.eq("id", user.getId()).set("password",newPassword).set("update_time",  DateTime.now().toString());
     int resInt = baseMapper.update(null,userUpdateWrapper);
      res = resInt>0?true:false;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
     return  false;
   }
    return  res;
  }

/**
 * @author  zhangchangzhong
 * @create  2020/1/6 16:28
 * @desc
 * 判断新密码和旧密码是否相等
 **/
private void comparePassword(User user,String newpassword ) throws AppException{
  try {
    if (user.getPassword().equals(PasswordUtils
            .getMd5Hash(new String(newpassword),user.getSalt()))) {
      throw new AppException(ErrorCodeEnum.UPDATEPASSWORD_USERERRO, "新密码和旧密码相同,不允许修改");
    }
  } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    throw new AppException(ErrorCodeEnum.UNEXCEPTED, "校验新旧密码是否相同异常");
  }
}

  @Override
  public IPage<User> selectPageVo(Page page, Wrapper<User> queryWrapper) {
    return baseMapper.selectPageVo(page,queryWrapper);
  }

  @Override
  public boolean saveRoleRelation(List<UserRoleRelationVO> userRoleRelations) {
    AtomicReference<Boolean> isRefresh = new AtomicReference<>(false);
    userRoleRelations.forEach(userRoleRelation -> {
      if (userRoleRelation.getAction() == 0) {
        baseMapper.deleteRoleRelation(userRoleRelation.getUserId(),
            roleService.getRoleByCode(userRoleRelation.getRoleCode()).getId());//会存在空指针异常,但让其报错
        isRefresh.set(true);
      } else if (userRoleRelation.getAction() == 1) {
        User user = baseMapper.selectById(userRoleRelation.getUserId());
        if (user != null) {
          baseMapper.saveRoleRelation(userRoleRelation.getUserId(),
              user.getUserName(),
              roleService.getRoleByCode(userRoleRelation.getRoleCode()).getId());
          isRefresh.set(true);
        }
      }else {
        new AppException(ErrorCodeEnum.ILLEGAL_PARAMETER);
      }
    });
    return isRefresh.get();
  }

  @Override
  public List<User> checkAppAccount(String appAccount, String password, String sourceCode) {
    return baseMapper.selectList(new QueryWrapper<User>().lambda()
        .eq(User::getAppAccount, appAccount)
        .eq(StringUtils.isNotEmpty(password),User::getPassword, password)
        .eq(StringUtils.isNotEmpty(sourceCode), User::getSourceId,
            datasourceService.getIdBySourceCode(sourceCode)));
  }


  public List<User> getUserByUsernameCode(String username, String sourceCode) {
    return baseMapper.selectList(new QueryWrapper<User>().lambda()
            .eq(StringUtils.isNotEmpty(username),User::getUserName, username)
            .eq(StringUtils.isNotEmpty(sourceCode), User::getSourceId,
                    datasourceService.getIdBySourceCode(sourceCode)));
  }

  public List<User> selectUsersByUsername(String username) {
    return baseMapper.selectList(new QueryWrapper<User>().lambda()
            .eq(StringUtils.isNotEmpty(username),User::getUserName, username));
  }

  /**
   * @author  zhangchangzhong
   * @create  2019/12/23 16:44
   * @desc
   * 根据账号类型和账号获取数据源
   **/
  @Override
  public List<Datasource> getDatasourceByUsernameAndType(String username, String type)throws AppException{
    List<User> existUsers = this.selectUsersByUsername(username);
    if(null==existUsers||existUsers.isEmpty()){//用户是否存在
      //与前端确认,此处返回空数组
      throw  new AppException(ErrorCodeEnum.USER_NOT_EXISTS,"用户不存在");
    }
    List<Integer> finalIds = new ArrayList<>();
    if(AucConst.DOMAIN_ACCOUNT.equals(type)){//域账号
      //找出是否存在关联此账号的域
      List<Domain> domains = iDomainService.getDomainList();
      if(null==domains||domains.isEmpty()){
        throw  new AppException(ErrorCodeEnum.RESULT_EMPTY,"系统未配置域");
      }
      List<Integer> domainSourceIds = domains.stream().map(domain->domain.getSourceId()).collect(Collectors.toList());
      finalIds =   existUsers.stream()
              .filter(user ->domainSourceIds.contains(user.getSourceId()))
              .map(user -> user.getSourceId())
              .collect(Collectors.toList()
              );
    }
    //根据数据源ID查询数据源信息
    return datasourceService.getBaseMapper().selectList(new QueryWrapper<Datasource>().lambda()
            .in( Datasource::getSourceId, finalIds) );
  }

    /**
     * @author  zhangchangzhong
     * @create  2019/12/23 16:45
     * @desc
     * 根据用户名,数据源,账号类型获取手机号
     **/
    @Override
    public User getMobilePhoneByUsernameAndScode(String username,String sourceCode,String type){
      User user  = new User();
      if(AucConst.DOMAIN_ACCOUNT.equals(type)) {//域账号
         user  =  this.getByUsernameAndSourceCode(username,sourceCode);
        if(null!=user){
          user.setPassword("");
          user.setSalt("");
          if(StringUtils.isNotEmpty(user.getMobilePhone())){
            return user;
          }else{
            throw  new AppException(ErrorCodeEnum.USER_ERROR,"用户手机号为空");
          }
        }else{
          throw  new AppException(ErrorCodeEnum.USER_ERROR,"用户不存在");
        }
      }
      return  user;
    }

    /**
     * @author  zhangchangzhong
     * @create  2019/12/24 11:35
     * @desc
     * 先校验手机验证码再修改密码
     **/
    public UpdatePasswordResponseDTO checkCodeAdUpdatePassword(String code, Integer userId,String newpassword){
      User user =  this.getById(userId);
      loginService.checkCode(code,user.getMobilePhone());
      UpdatePasswordResponseDTO  updatePasswordResponseDTO = this.updateAllPassword(userId,newpassword);
      return updatePasswordResponseDTO;
    }


  /**
   * @author  zhangqi
   * @create  2020/03/03 16:31
   * @desc
   * 检测域密码是否过期
   **/
  @Override
  public boolean isExpiredOfPassword(String adAccount, String adSource) {
    Boolean res = false;
    try {
      SoapClient client = SoapClient.create(isExpiredUrl)
          // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
          .setMethod("tem:CheckAdUserPasswordExpired", "http://tempuri.org/")
          // 设置参数，此处自动添加方法的前缀：web
          .setParam("adSource", adSource)
          .setParam("adAccount", adAccount);

      // 发送请求，参数true表示返回一个格式化后的XML内容
      // 返回内容为XML字符串，可以配合XmlUtil解析这个响应
      String s =client.send(true);
      Document resdoc = XmlUtil.parseXml (s);
      res= (Boolean) XmlUtil.getByXPath("/CheckAdUserPasswordExpiredResponse/CheckAdUserPasswordExpiredResult", resdoc, XPathConstants.BOOLEAN);
    }catch (Exception e){
      res = false;
    }
    return res;
  }
}
