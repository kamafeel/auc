package com.auc.web.security;

import com.auc.common.AppException;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.JsonUtil;
import com.auc.web.help.Result;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 认证过的用户访问无权限资源时的异常
 * @author zhangqi
 */
@Slf4j
public class JWTAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.error("accessDenied  path : {}  accessDeniedException is {}", request.getServletPath(),accessDeniedException.toString());

    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter()
        .write(JsonUtil.toStr(Result.failed(new AppException(ErrorCodeEnum.ACCESS_DENIED))));
    response.flushBuffer();
  }
}
