package com.auc.web.help;


import com.auc.common.Assert;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.web.security.JWTAuthenticationToken;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取上下文信息
 * @author zhangqi
 */
public class ContextUtil {

  public static String getCurrentUserId() {
    return getJWTAuthentication().getUserId();
  }

  public static String getCurrentUserName() {
    return getJWTAuthentication().getUserName();
  }

  /**
   * 获取角色
   * @return
   */
  public static List<String> getRoles(){
    return getJWTAuthentication().getAuthorities().stream()
        .filter(a->a.getAuthority().startsWith("ROLE_"))
        .map(a->a.getAuthority().replaceFirst("ROLE_","")).collect(Collectors.toList());
  }

  public static JWTAuthenticationToken getJWTAuthenticationNoException() {
    JWTAuthenticationToken jt = null;
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Assert.isTrue(authentication != null && authentication.isAuthenticated()
          && authentication instanceof JWTAuthenticationToken, ErrorCodeEnum.USER_NOT_LOGIN);
      jt = (JWTAuthenticationToken) authentication;
    } catch (Exception e) {
    }
    return jt;
  }


  public static JWTAuthenticationToken getJWTAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Assert.isTrue(authentication != null && authentication.isAuthenticated()
        && authentication instanceof JWTAuthenticationToken, ErrorCodeEnum.USER_NOT_LOGIN);
    return (JWTAuthenticationToken) authentication;
  }
}
