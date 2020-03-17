package com.auc.web.security;


import com.auc.common.constants.AucConst;
import com.auc.service.RedisService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * 会被添加为最后一个logoutHandler
 * token失效的处理
 * @author zhangqi
 */
@Component
@Slf4j
public class JWTLogoutHandler implements LogoutHandler {

  @Autowired
  private RedisService redisService;

  @Autowired
  private TokenGenerator tokenGenerator;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String token = request.getHeader(AucConst.JWT_AUTH_TOKEN_KEY_NAME);
    log.info("{},will be delete from redis",token);
    // 删除token缓存
    redisService.delete(AucConst.JWT_USER_REDIS_PATH + tokenGenerator.parseJWTToken(token).get("userId"));
    // 失效token密钥
    redisService.delete(AucConst.JWT_SECRET_REDIS_PATH + token);
  }
}
