package com.auc.web.controller;

import com.auc.common.AppException;
import com.auc.common.constants.AucConst;
import com.auc.common.constants.ValidateMessage;
import com.auc.common.enums.DataSourceEnum;
import com.auc.common.enums.DomainEnum;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.JsonUtil;
import com.auc.common.utils.RegularExpression;
import com.auc.dao.Client;
import com.auc.dao.Datasource;
import com.auc.dao.User;
import com.auc.domain.dto.requestdto.LoginBaseRequestDTO;
import com.auc.domain.dto.requestdto.MPLoginRequestDTO;
import com.auc.domain.dto.requestdto.ProxyReqRequestDTO;
import com.auc.domain.dto.requestdto.SmsCodeRequestDTO;
import com.auc.domain.dto.requestdto.UserDomainLoginRequestDTO;
import com.auc.domain.dto.requestdto.UserSystemLoginRequestDTO;
import com.auc.domain.dto.responsedto.*;
import com.auc.dubbo.user.service.IBaseUserService;
import com.auc.dubbo.user.dao.BaseUser;
import com.auc.service.IClientService;
import com.auc.service.IDatasourceService;
import com.auc.service.IUserService;
import com.auc.service.LoginService;
import com.auc.service.impl.UserServiceImpl;
import com.auc.web.help.Result;
import com.auc.web.security.TokenGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author zhangqi
 *
 */
@Slf4j
@RestController
@Api(value = "登录模块", tags = "登录模块")
@RequestMapping("api/auth")
public class AuthController {

  private static final Pattern USERNAME_PATTERN = Pattern.compile("(?i)[^a-zA-Z]loginName=([^&=?]+)");

  @Resource
  private LoginService loginService;
  @Resource
  private IClientService IClientService;
  @Resource
  private TokenGenerator tokenGenerator;
  @Autowired
  public IDatasourceService datasourceService;
  @Reference(version = "${service.version.user}", check = false)
  private IBaseUserService baseUserService;

  @Autowired
  private IUserService userService;

  @ApiOperation(value = "获取域列表(废弃)", notes = "获取域列表(废弃)")
  @RequestMapping(value = "getDomainList", method = RequestMethod.GET)
  @ResponseBody
  @Deprecated
  public Result getDomainList() {
    return Result.success(Arrays.toString(DomainEnum.values()));
  }

  /**
   * 域登录
   */
  @ApiOperation(value = "域登录", notes = "域登录")
  @RequestMapping(value = "domainLogin", method = RequestMethod.POST)
  @ResponseBody
  public Result<LoginTokenResponseDTO> domainLogin(@RequestBody @Valid UserDomainLoginRequestDTO dto) {
    Client client = IClientService.getByClientId(dto.getClient_id());
    if(client == null){
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"第三方信息错误");
    }
    DomainLoginInfoResponseDTO loginInfo = loginService.domainLogin(dto);
    if (loginInfo == null) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"域登录失败");
    }
    loginInfo.setState(dto.getState());
    LoginTokenResponseDTO res = tokenGenerator.generateLoginToken(loginInfo,null);//不允许用自定义密钥,考虑后面的SSO

    return Result.success(res);
  }

  /**
   *  系统登录
   */
  @ApiOperation(value = "应用系统账号登录", notes = "应用系统账号登录")
  @RequestMapping(value = "appAccountLogin", method = RequestMethod.POST)
  @ResponseBody
  public Result<LoginTokenResponseDTO> appAccountLogin(@RequestBody @Valid UserSystemLoginRequestDTO dto) {
    Client client = IClientService.getByClientId(dto.getClient_id());
    if(client == null){
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"第三方信息错误");
    }
    LoginInfoResponseDTO loginInfo = loginService.appAccountLogin(dto);
    if (loginInfo == null) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"系统登录失败");
    }
    loginInfo.setState(dto.getState());
    LoginTokenResponseDTO res = tokenGenerator.generateLoginToken(loginInfo,null);//不允许用自定义密钥,考虑后面的SSO
    return Result.success(res);
  }

  /**
   *  系统登录
   */
  @ApiOperation(value = "应用系统账号多数据源检测", notes = "应用系统账号多数据源检测")
  @RequestMapping(value = "appAccountResource/{clientId}/{appAccount}", method = RequestMethod.POST)
  @ResponseBody
  public Result<List<Map<String, String>>> appAccountResource(@PathVariable("clientId") String clientId,
      @PathVariable("appAccount") String appAccount) {
    List<Map<String, String>> rMap = userService.getDataSourceByAppAccount(appAccount);
    if(rMap == null || rMap.isEmpty()){
      return Result.success(rMap);
    }
    Client c = IClientService.getByClientId(clientId);
    //存在自定义登录范围限制
    if (c != null && c.isCustomLogin()) {
      try {
        JsonNode jn = JsonUtil.toJsonNode(c.getCustomLoginInfo()).get("SYSTEM");
        HashSet<String> l = Sets.newHashSet();
        if (jn.isArray()) {
          for(JsonNode objNode : jn){
            l.add(objNode.asText());
          }
        }
        //过滤取交集
        return Result.success(rMap.stream().filter(m->l.contains(m.get("source_code"))).collect(Collectors.toList()));
      } catch (Exception e) {
        return Result.failed(ErrorCodeEnum.UNEXCEPTED, e.getMessage());
      }
    }
    return Result.success(rMap);
  }

  /**
   * 登录代理
   */
  @ApiOperation(value = "登录代理(token生成可选)", notes = "登录代理(token生成可选)")
  @RequestMapping(value = "proxyLogin", method = RequestMethod.POST)
  @ResponseBody
  public Result<Object> proxyLogin(@RequestBody @Valid ProxyReqRequestDTO dto) {
    Client client = IClientService.getByClientId(dto.getClient_id());
    if(client == null){
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"第三方信息错误");
    }
    LoginInfoResponseDTO rl = loginService.proxyLogin(dto);
    if(dto.isCreateToken()){
      return Result.success(tokenGenerator.generateLoginToken(rl,null));
    }else{
      return Result.success(true);
    }
  }

  @ApiOperation(value = "手机号登录", notes = "手机号登录")
  @RequestMapping(value = "mobilePhoneLogin", method = RequestMethod.POST)
  @ResponseBody
  public Result<LoginTokenResponseDTO> mobilePhoneLogin(@RequestBody @Valid MPLoginRequestDTO dto) {
    Client client = IClientService.getByClientId(dto.getClient_id());
    if(client == null){
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"第三方信息错误");
    }
    MpLoginInfoResponseDTO loginInfo = loginService.mpLogin(dto);
    if (loginInfo == null) {
      return Result.failed(ErrorCodeEnum.PHONE_CODE_IS_WRONG);
    }
    LoginTokenResponseDTO res = tokenGenerator.generateLoginToken(loginInfo, null);
    return Result.success(res);
  }

  @ApiOperation(value = "获取手机号所属数据源", notes = "获取手机号所属数据源")
  @RequestMapping(value = "getDataSourceByMobilePhone/{clientId}/{mobilePhone}", method = RequestMethod.GET)
  @ResponseBody
  public Result<List<Map<String, String>>> getDataSourceByMobilePhone(@PathVariable("clientId") String clientId,
      @PathVariable("mobilePhone") String mobilePhone) {
    if(!Pattern.matches(AucConst.REGEX_MOBILE_EXACT,mobilePhone)){
      return Result.failed(ErrorCodeEnum.ILLEGAL_PARAMETER,ValidateMessage.MOBILE_PHONE_INCORRECT);
    }
    List<Map<String, String>> rMap = userService.getDataSourceByMobilePhone(mobilePhone);
    if(rMap == null || rMap.isEmpty()){
      return Result.success(rMap);
    }
    Client c = IClientService.getByClientId(clientId);
    //存在自定义登录范围限制
    if (c != null && c.isCustomLogin()) {
      try {
        JsonNode jn = JsonUtil.toJsonNode(c.getCustomLoginInfo()).get("MOBILE");
        HashSet<String> l = Sets.newHashSet();
        if (jn.isArray()) {
          for(JsonNode objNode : jn){
            l.add(objNode.asText());
          }
        }
        //过滤取交集
        return Result.success(rMap.stream().filter(m->l.contains(m.get("source_code"))).collect(Collectors.toList()));
      } catch (Exception e) {
        return Result.failed(ErrorCodeEnum.UNEXCEPTED, e.getMessage());
      }
    }
    return Result.success(rMap);
  }


  /**
   * 刷新token
   */
  @ApiOperation(value = "刷新token(header里面aucJWTToken传刷新token)", notes = "刷新token(header里面aucJWTToken传刷新token)")
  @RequestMapping(value = "refreshToken", method = RequestMethod.POST)
  @ResponseBody
  public Result<RefreshTokenResponseDTO> refreshToken(HttpServletRequest request) {
    return Result.success(tokenGenerator.refreshToken(request.getHeader(AucConst.JWT_AUTH_TOKEN_KEY_NAME)));
  }

  /**
   * 获取短信验证码（登录）
   */
  @ApiOperation(value = "获取短信验证码（登录、密码重置）", notes = "获取短信验证码（登录、密码重置）")
  @RequestMapping(value = "getSMSCode", method = RequestMethod.POST)
  @ResponseBody
  public Result<Boolean> getSMSCode(@RequestBody @Valid SmsCodeRequestDTO dto) {
    return Result.success(loginService.sendSMSCode(dto));
  }

  /**
   * 获取系统数据源
   */
  @ApiOperation(value = "获取系统数据源", notes = "获取系统数据源")
  @RequestMapping(value = "datasource", method = RequestMethod.GET)
  @ResponseBody
  public Result<String> datasource() {
    return Result.success(Arrays.toString(DataSourceEnum.values()));
  }


  /**
   * 电器费控验证token接口，延续智慧办公验证逻辑
   * @param base64Security
   * @param request
   * @return
   */
  @ApiOperation(value = "电器费控验证token接口", notes = "电器费控验证token接口")
  @RequestMapping(value = "checkToken", method = RequestMethod.GET)
  @ResponseBody
  public JsonObject checkShareOldEmsBillToken(@RequestParam(value="t",required=false) String base64Security, HttpServletRequest request){
    JsonObject result = new JsonObject();
    if(!StringUtils.isEmpty(base64Security)){
      try {
        String userInfoString = new String(Base64.decodeBase64(base64Security), StandardCharsets.UTF_8);
        if(!StringUtils.isEmpty(userInfoString)){
          Matcher matcher = USERNAME_PATTERN.matcher(userInfoString);
          if(matcher.find()){
            String userName = matcher.group(1).trim();
            String jwt = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(jwt) && jwt.startsWith("Bearer ")) {
              try {
                jwt = jwt.replaceAll("(?i)^Bearer\\s*", "");
                Claims claims = tokenGenerator.parseJWTToken(jwt, base64Security);
                if(claims != null && claims.getSubject() != null){
                  Gson gson = new Gson();
                  JsonObject jsonObject = gson.fromJson(claims.getSubject(), JsonObject.class);
                  if(jsonObject != null){
                    String userName2 = jsonObject.get("userName").getAsString();
                    //String sourceId = jsonObject.getString("sourceId");
                    if(userName2 != null  && userName.equals(userName2.trim())){
                      result.addProperty("obj", "S");
                      result.addProperty("success", true);
                      return result;
                    }else{
                      result.addProperty("msg", "权限信息不匹配!");
                    }
                  }
                }
                result.addProperty("msg","没有权限信息!");
              } catch (Exception e) {
                e.printStackTrace();
                result.addProperty("success",false);
                result.addProperty("msg","校验权限发生异常!");
              }
            }else{
              result.addProperty("msg","缺少权限信息!");
            }
          }
        }
      } catch (Exception e1) {
        e1.printStackTrace();
        result.addProperty("msg","解析权限出错!");
      }
    }else{
      result.addProperty("msg","缺少参数oldEmsParam!");
    }
    result.addProperty("success",false);
    result.addProperty("obj","F");
    return result;
  }

  /**
   * 电器OA，互联网OA用token获取用户信息接口,延续智慧办公逻辑
   * @param request
   * @return
   */
  @ApiOperation(value = "电器OA，互联网OA用token获取用户信息接口", notes = "电器OA，互联网OA用token获取用户信息接口")
  @RequestMapping(value = "getUserByToken", method = RequestMethod.POST)
  @ResponseBody
  public BaseUser findUser(HttpServletRequest request){
    long  startTime = System.currentTimeMillis();
    log.debug("解析token开始:"+startTime);
    BaseUser baseUser = null;
    String jwt = request.getHeader("Authorization");
    if (jwt != null &&jwt.startsWith("Bearer ")) {
      try {
        jwt = jwt.substring(jwt.indexOf(" "));
        String jsonStr = tokenGenerator.parseJWTToken(jwt, "secret").getSubject();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonStr, JsonObject.class);
        String userName = jsonObject.get("userName").getAsString();
        String sourceId = jsonObject.get("sourceId").getAsString();
        log.debug("解析token耗时:"+(System.currentTimeMillis()-startTime)+"ms");
        log.debug("获取"+userName+"用户信息开始开始:"+System.currentTimeMillis());
        baseUser = baseUserService.getByUsernameAndSourceCode(userName, datasourceService.getCodeBySourceId(Integer.parseInt(sourceId)));
        log.debug("到获取"+userName+"用户总耗时:"+(System.currentTimeMillis()-startTime)+"ms");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    log.debug("接口请求总耗时:"+(System.currentTimeMillis()-startTime)+"ms");
    return baseUser;
  }
  /**
   * @author  zhangchangzhong
   * @create  2019/12/23 11:42
   * @desc
   * 根据账号类型和账号获取数据源
   **/
  @ApiOperation(value = "根据账号类型和账号获取数据源", notes = "根据账号类型和账号获取数据源")
  @RequestMapping(value = "getDatasourceByUsernameAndType",method = RequestMethod.GET)
  @ResponseBody
  public Result getDatasourceByUsernameAndType(@ApiParam(value = "用户名", required = true)@RequestParam String username,@ApiParam(value = "账号类型: 域账号(domain)", required = true)@RequestParam String type){
    List<Datasource> datasourceList = new ArrayList<>();
    try {
      datasourceList = userService.getDatasourceByUsernameAndType(username,type);
    }catch (AppException ae){
      return Result.failed(ae.getCode(),ae.getMessage());
    }
    return Result.success(datasourceList);
  }


  /**
   * @author  zhangchangzhong
   * @create  2019/12/23 17:02
   * @desc
   * 根据账号类型和账号和数据源编码获取手机号
   **/
  @ApiOperation(value = "根据账号类型和账号和数据源编码获取手机号", notes = "根据账号类型和账号和数据源编码获取手机号")
  @RequestMapping(value = "getMobilePhoneByUsernameAndScode",method = RequestMethod.GET)
  @ResponseBody
  public Result getMobilePhoneByUsernameAndScode(@ApiParam(value = "用户名", required = true)@RequestParam String username,@ApiParam(value = "数据源编码", required = true)@RequestParam String sourceCode,@ApiParam(value = "账号类型: 域账号(domain)", required = true)@RequestParam  String type){

    try {
      User  user = userService.getMobilePhoneByUsernameAndScode(username,sourceCode,type);
      return Result.success(user);
    }catch (AppException ae){
      return Result.failed(ae.getCode(),ae.getMessage());
    }
  }

  /**
   * @author  zhangchangzhong
   * @create  2019/12/23 17:02
   * @desc
   * 根据手机号和验证码校验验证码
   **/
  @ApiOperation(value = "校验验证码", notes = "校验验证码")
  @RequestMapping(value = "checkCodeByMobilePhone",method = RequestMethod.GET)
  @ResponseBody
  public Result checkCodeByMobilePhone(@ApiParam(value = "验证码", required = true)@RequestParam String code,@ApiParam(value = "手机号", required = true)@RequestParam  String mobilePhone){

    try {
      loginService.checkCode(code,mobilePhone);
      return Result.success(true);
    }catch (AppException ae){
      return Result.failed(ae.getCode(),ae.getMessage());
    }
  }

  /**
   * @author  zhangchangzhong
   * @create  2019/12/24 16:37
   * @desc
   **/
  @ApiOperation(value = "找回密码", notes = "找回密码")
  @RequestMapping(value = "restPassword",method = RequestMethod.POST)
  @ResponseBody
  public Result<UpdatePasswordResponseDTO> restPassword(@ApiParam(value = "验证码", required = true)@RequestParam String code,@ApiParam(value = "用户id", required = true)@RequestParam  Integer userId,@ApiParam(value = "新密码", required = true)@RequestParam  String nowPassword){
    boolean isMatch = RegularExpression.isContainsSpecialCharacterAndNumberAndCharacter(nowPassword);
    if (isMatch) {//密码规则
      try {
        UpdatePasswordResponseDTO updatePasswordResponseDTO = userService.checkCodeAdUpdatePassword(code,userId,nowPassword);
        return Result.success(updatePasswordResponseDTO);
      }catch (AppException ae){
        return Result.failed(ae.getCode(),ae.getMessage());
      }
    } else {
      return Result.failed(ErrorCodeEnum.PASSWORD_NOT_DISQUALIFIED,"密码太过简单,必须包含特殊字符、数字、字母的组合");
    }
  }


  @ApiOperation(value = "获取第三方系统的登录范围", notes = "获取第三方系统的登录范围")
  @RequestMapping(value = "getClientLoginType/{clientId}",method = RequestMethod.GET)
  @ResponseBody
  public Result<JsonNode> getClientLoginType(@PathVariable("clientId") String clientId) {
    Client c = IClientService.getByClientId(clientId);
    try {
      if (c != null && c.isCustomLogin()) {
        return Result.success(JsonUtil.toJsonNode(c.getCustomLoginInfo()));
      } else {
        return Result.success(JsonUtil.toJsonNode(AucConst.CLIENT_LOGIN_TYPE_LIMIT));
      }
    } catch (IOException e) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED, e.getMessage());
    }
  }
}
