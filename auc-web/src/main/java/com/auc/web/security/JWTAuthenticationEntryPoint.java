package com.auc.web.security;

import com.auc.common.AppException;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.JsonUtil;
import com.auc.web.help.Result;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 匿名用户访问无权限资源时的异常
 * @author zhangqi
 */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter()
        .write(JsonUtil.toStr(Result.failed(new AppException(ErrorCodeEnum.USER_NOT_LOGIN))));
    response.flushBuffer();
  }

}
