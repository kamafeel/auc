package com.auc.service.impl.strategy;

import com.auc.common.utils.JsonUtil;
import com.auc.dao.User;
import com.auc.domain.dto.requestdto.LoginBaseRequestDTO;
import com.auc.domain.dto.requestdto.UserDomainLoginRequestDTO;
import com.auc.domain.dto.requestdto.UserSystemLoginRequestDTO;
import com.auc.domain.dto.responsedto.LoginInfoResponseDTO;
import com.auc.dubbo.user.dao.BaseUser;
import com.auc.service.IClientLoginStrategyService;
import com.auc.service.IUserService;
import com.auc.utils.HttpUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Base64;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AUC特殊处理逻辑
 *
 * 登录刷新密码功能,功能描述：
 * 自动同步合并用户域密码
 */
@Service
@Slf4j
public class AUCLoginStrategyServiceImpl implements IClientLoginStrategyService {

  @Autowired
  private HttpUtils httpUtils;
  @Autowired
  private IUserService iUserService;
  private static final String DOMAINS_LOGIN_PATH = "/newWeb/auth/domainsLogin.do";

  @Override
  public <T extends LoginBaseRequestDTO> void loginStrategy(T dto, User user, String client_id) {
    /**
    try {
      HashMap<String,Object> params = new HashMap();
      params.put("userName", Base64.getEncoder().encodeToString(dto.getUserName().getBytes()));
      params.put("passWord", Base64.getEncoder().encodeToString(new String(Base64.getDecoder().decode(dto.getPassword())).getBytes()));
      params.put("domainCode", dto.getDomainCode().getCode());
      params.put("rememberMe", "false");
      log.info("EMS Req:{}", JsonUtil.toPrettyStr(params));
      log.info("EMS Res:{}",JsonUtil.toPrettyStr(httpUtils.formPost(url + DOMAINS_LOGIN_PATH, params)));
    } catch (Exception e) {
      log.error("",e);
    }
     **/
    this.loginStrategyUpdatePwd(dto,user,client_id);
  }

  /**
   * @author  zhangchangzhong
   * @create  2019/12/26 9:37
   * @desc
   * 登录成功之后刷新密码
   **/

  private  <T extends LoginBaseRequestDTO> void loginStrategyUpdatePwd(T dto, User user, String client_id) {
    String password = "";
    //判断为域登录或者系统登录时候获取到密码才刷新密码
    if (UserDomainLoginRequestDTO.class.getName().equals(dto.getClass().getName())){
     UserDomainLoginRequestDTO userDomainLoginRequestDTO = (UserDomainLoginRequestDTO)dto;
      password=new String( Base64.getDecoder().decode(userDomainLoginRequestDTO.getPassword()));
    }
    if (UserSystemLoginRequestDTO.class.getName().equals(dto.getClass().getName())){
      UserSystemLoginRequestDTO userSystemLoginRequestDTO = (UserSystemLoginRequestDTO)dto;
      password=new String( Base64.getDecoder().decode(userSystemLoginRequestDTO.getPassword()));
    }
    if(StringUtils.isNotEmpty(password)){
      iUserService.updateAllPassword(user.getId(),password);
    }

  }
}
