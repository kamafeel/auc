package com.auc.web.security;

import com.auc.common.Assert;
import com.auc.common.enums.ErrorCodeEnum;
import javax.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * JWT验证实现
 * @author zhangqi
 */
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

  @Resource
  private TokenGenerator tokenGenerator;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    JWTAuthenticationToken jwtAuthentication = (JWTAuthenticationToken) authentication;
    Assert.isNotNull(jwtAuthentication, ErrorCodeEnum.ILLEGAL_PARAMETER);
    return tokenGenerator.generateAuthenticationToken(jwtAuthentication.getJwtToken());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JWTAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
