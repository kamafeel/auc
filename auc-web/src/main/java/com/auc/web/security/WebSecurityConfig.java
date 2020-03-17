package com.auc.web.security;

import com.auc.common.enums.RoleEnum;
import com.auc.web.controller.DefaultErrorController;
import java.util.Arrays;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
//开启方法级的权限注解  性设置后控制器层的方法前的@PreAuthorize("hasRole('admin')") 注解才能起效
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private JWTAuthenticationFilter jwtAuthenticationFilter;
  @Resource
  private JWTAuthenticationProvider jwtAuthenticationProvider;
//  @Resource
//  private DomainLoginFilter domainLoginFilter;
  @Autowired
  private JWTLogoutHandler jwtLogoutHandler;

  /**
   * 忽略权限管理
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers(DefaultErrorController.ERROR_PATH,
            "/api/auth/**",
            "/api/imageCode/**",
            "/common/**",
            "/resources/static/**",
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
        );
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 由于使用的是JWT，不需要csrf
    http.csrf().disable()
        // by default uses a Bean by the name of corsConfigurationSource
        .cors().and()
        // 权限不足处理类
        .exceptionHandling().accessDeniedHandler(new JWTAccessDeniedHandler()).and()
        // 认证失败处理类
        .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint()).and()
        // 基于token，不需要session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        // 域登录的过滤器
        //.addFilterBefore(domainLoginFilter, AbstractPreAuthenticatedProcessingFilter.class)
        // token过滤器
        .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
        // token提供者
        .authenticationProvider(jwtAuthenticationProvider).authorizeRequests()
        .filterSecurityInterceptorOncePerRequest(true)
        // 路径和角色
        .antMatchers("/api/system/**")
        .hasRole(RoleEnum.SUPER.name())//设置访问角色
        //.hasAnyAuthority(RoleEnum.SUPER.name())//设置访问权限
        // 除上面外的所有请求全部需要鉴权认证
        .anyRequest().authenticated().and()
        // 登出配置
        .logout().logoutUrl("/api/logout")
        .addLogoutHandler(jwtLogoutHandler)
        .logoutSuccessHandler(new JWTLogoutSuccessHandler());

    // 禁用缓存
    http.headers().cacheControl();
  }

  /**
   * 角色继承
   * @return
   */
  @Bean
  RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    String hierarchy = "ROLE_SUPER > ROLE_ADMIN " + System.lineSeparator() + " ROLE_ADMIN > ROLE_USER";
    roleHierarchy.setHierarchy(hierarchy);
    return roleHierarchy;
  }

  /**
   * 跨域的配置
   * @return
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.addExposedHeader("Location"); // 暴露 Location header，让XMLHttpRequest对象能拿到
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
