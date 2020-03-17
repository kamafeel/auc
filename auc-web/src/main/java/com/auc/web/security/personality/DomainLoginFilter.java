//package com.auc.web.security.personality;
//
//import com.alibaba.fastjson.JSONObject;
//import com.auc.common.AppException;
//import com.auc.web.help.Result;
//import com.auc.web.security.JWTAuthenticationToken;
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.filter.OncePerRequestFilter;
//
///**
// * 域登录过滤器
// * @author zhangqi
// *
// */
////类的名字是以两个或以上的大写字母开头,需要指定bean名称，避免误会
//@Component("domainLoginFilter")
//@Slf4j
//public class DomainLoginFilter extends OncePerRequestFilter implements InitializingBean {
//
//  private static final String DOMAIN_LOGIN_URL = "/auth/domainLogin";
//  /**
//   * 验证请求url与配置的url是否匹配的工具类
//   */
//  private AntPathMatcher pathMatcher = new AntPathMatcher();
//
//  @SuppressWarnings("rawtypes")
//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//      FilterChain filterChain) throws ServletException, IOException {
//    if (pathMatcher.match(DOMAIN_LOGIN_URL, request.getRequestURI())) {
//      // TODO 密码解密
//    }else{
//      //let it go
//      filterChain.doFilter(request, response);
//    }
//  }
//}
