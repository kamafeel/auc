package com.auc.web.handler;

import com.auc.common.AppException;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.dao.Client;
import com.auc.dao.User;
import com.auc.service.IUserService;
import com.auc.web.security.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 人资系统token生成handler
 */
@Component
@Slf4j
@HandlerType("HUMAN_CAPITAL")
public class HumanCapitalTokenHandler extends TokenHandler {

  @Autowired
  private IUserService userService;

  @Autowired
  private TokenGenerator tokenGenerator;

  @Override
  public String handle(Client client, String userName, String sourceCode) {
    // step 1 需要查询出
    User user = userService.getByUsernameAndSourceCode(userName, sourceCode);
    if (user == null) {
      throw new AppException(ErrorCodeEnum.RESULT_EMPTY, "未查询到对应用户");
    }
    long expiration = 24 * 60 * 60 * 1000L;// 默认1天过期
    String token = tokenGenerator.createJWT(user.getPsId(), user.getSourceId(), "GM020-OA", "GM-PS",
        expiration, client.getClientJwtSecret());

    return token;
  }

}
