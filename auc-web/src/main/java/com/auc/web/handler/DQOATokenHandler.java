package com.auc.web.handler;

import com.auc.common.AppException;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.dao.Client;
import com.auc.service.IDatasourceService;
import com.auc.web.security.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 电器费控token生成handler
 */
@Component
@HandlerType("DQ_OA")
@Slf4j
public class DQOATokenHandler extends TokenHandler {

  @Autowired
  private TokenGenerator tokenGenerator;

  @Autowired
  public IDatasourceService datasourceService;

  /**
   * 电器OA单点登录地址
   */
  private static final String SSO_URL = "/HOWebApp/CostSSO/Sig/SignIn.aspx";

  /**
   * 单点登录成功后要跳转的URL地址
   */
  private static final String DEFAULT_URL = "/HOWebApp/GomeOAPortal/default.aspx?uns=1";



  @Override
  public String handle(Client client, String userName, String sourceCode) {

    try {
      return new StringBuilder(SSO_URL)
          .append("?token=")
          .append("Bearer ")
          .append(tokenGenerator.createJWT(userName,datasourceService.getIdBySourceCode(sourceCode),
              "audience", "issuer", 180000L, client.getClientJwtSecret()))
          .append("&ru=").append(URLEncoder.encode(client.getClientLoginUrl() + DEFAULT_URL, "UTF-8"))
          .toString();
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
      e.printStackTrace();
      throw new AppException(ErrorCodeEnum.UNEXCEPTED, "电器OA token异常");
    }
  }

}


