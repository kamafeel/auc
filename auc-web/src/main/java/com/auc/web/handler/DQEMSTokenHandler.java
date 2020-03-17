package com.auc.web.handler;

import com.auc.common.enums.DataSourceEnum;
import com.auc.dao.Client;
import com.auc.service.IDatasourceService;
import com.auc.web.help.ContextUtil;
import com.auc.web.security.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 电器费控token生成handler
 */
@Component
@HandlerType("DQ_EMS")
@Slf4j
public class DQEMSTokenHandler extends TokenHandler {

  @Autowired
  private TokenGenerator tokenGenerator;

  @Autowired
  public IDatasourceService datasourceService;

  @Override
  public String handle(Client client, String userName, String sourceCode) {
    String base64UserName = getBase64("&loginName=" + userName );
    return new StringBuilder("&t=")
        .append(base64UserName)
        .append("&token=")
        .append("Bearer ")
        .append(tokenGenerator.createJWT(userName,datasourceService.getIdBySourceCode(sourceCode),
            "audience", "issuer", 180000L, base64UserName))
        .toString();
  }

  /**
   * @param str
   * @return
   * @method getBase64
   * @description 旧版（电器费控）返回的参数通过base64转义
   */
  private String getBase64(String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    }
    Base64 base64 = new Base64();
    byte[] textByte = str.getBytes(StandardCharsets.UTF_8);
    return base64.encodeToString(textByte);
  }

}


