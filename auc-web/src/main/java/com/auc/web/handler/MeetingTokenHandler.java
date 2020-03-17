package com.auc.web.handler;

import com.auc.dao.Client;
import com.auc.dubbo.user.service.IBaseUserService;
import com.auc.service.IDatasourceService;
import com.auc.web.security.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 视频会议系统token生成handler
 */
@Component
@Slf4j
@HandlerType("MEETING_UNION")
public class MeetingTokenHandler extends TokenHandler {

  @Reference(version = "${service.version.user}", check = false)
  private IBaseUserService baseUserService;

  @Autowired
  private TokenGenerator tokenGenerator;
  @Autowired
  public IDatasourceService datasourceService;

  @Override
  public String handle(Client client, String userName, String sourceCode) {

    long expiration = 24 * 60 * 60 * 1000L;// 默认1天过期
    String token = tokenGenerator.createJWT(userName, datasourceService.getIdBySourceCode(sourceCode), "GM-OA", "GM-MEET",
        expiration, client.getClientJwtSecret());
    return token;
  }

}
