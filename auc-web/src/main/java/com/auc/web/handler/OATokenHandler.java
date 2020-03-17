package com.auc.web.handler;

import com.auc.common.utils.RSAUtils;
import com.auc.dao.Client;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 智慧办公的登录处理
 * @author zhangqi
 */
@Component
@HandlerType("OA")
@Slf4j
public class OATokenHandler extends TokenHandler {

  @Override
  public String handle(Client client, String userName, String sourceCode) {
    try {
      return new StringBuilder("?username=")
          .append(RSAUtils.encrypt(
              Base64.getEncoder().encodeToString(userName.getBytes("UTF-8"))
              ,client.getClientJwtSecret()))
          .append("&sourceCode=")
          .append(sourceCode)
          .toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


}


