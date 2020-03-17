package com.auc.web.security;

import com.auc.common.utils.JsonUtil;
import com.auc.web.help.Result;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


import lombok.extern.slf4j.Slf4j;

/**
 * 登出成功的处理
 * @author zhangqi
 */
@Slf4j
public class JWTLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("logout success");

    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter().write(JsonUtil.toStr(Result.success()));
    response.flushBuffer();
  }
}
