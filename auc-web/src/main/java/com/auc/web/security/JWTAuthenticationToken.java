package com.auc.web.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 自定义的token实现
 * @author zhangqi
 *
 */
@Slf4j
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 689177934199815232L;
  @Getter
  private String userId;
  @Getter
  private String sourceId;
  @Getter
  private String sourceCode;
  @Getter
  private String userName;
  @Getter
  private String personnelCode;
  @Getter
  private String jwtToken;

  public JWTAuthenticationToken(String jwtToken) {
    super(null);
    this.jwtToken = jwtToken;
    setAuthenticated(false);
    log.info("JWTAuthenticationToken setAuthenticated ->false loading ...");
  }

  public JWTAuthenticationToken(String userId, String userName, String personnelCode, String sourceId, String sourceCode
      , Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.userId = userId;
    this.userName = userName;
    this.sourceId = sourceId;
    this.sourceCode = sourceCode;
    this.personnelCode = personnelCode;
    super.setAuthenticated(true);
    log.info("JWTAuthenticationToken setAuthenticated ->true loading ...");
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    if (isAuthenticated) {
      //不允许直接设置,需使用构造函数
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }

    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return userName;
  }

}
