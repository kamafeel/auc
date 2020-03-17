package com.auc.web.security;

import com.auc.common.AppException;
import com.auc.common.constants.AucConst;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.Java8DateUtil;
import com.auc.common.utils.JsonUtil;
import com.auc.common.utils.SnowFlake;
import com.auc.dao.Operational;
import com.auc.dao.Role;
import com.auc.domain.dto.responsedto.LoginInfoResponseDTO;
import com.auc.domain.dto.responsedto.LoginTokenResponseDTO;
import com.auc.domain.dto.responsedto.RefreshTokenResponseDTO;
import com.auc.service.IOperationalService;
import com.auc.service.IRoleService;
import com.auc.service.RedisService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangqi
 */
@Component
@Slf4j
public class TokenGenerator {

  @Resource
  private RedisService redisService;
  @Value("${jwt.expireMinutes.authToken}")
  private int authTokenExpireMinutes;
  @Value("${jwt.expireMinutes.refreshToken}")
  private int refreshTokenExpireMinutes;

  /**
   * 基于当前登录用户的信息，生成完整登录令牌信息
   * 实现踢出的功能,每次登陆都将踢出用户之前的token（如果存在的话）
   */
  public <T extends LoginInfoResponseDTO> LoginTokenResponseDTO generateLoginToken(T e, String clientSecret) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", e.getUserId());
    claims.put("sourceId", e.getSourceId());
    claims.put("sourceCode", e.getSourceCode());
    claims.put("userName", e.getUserName());
    claims.put("personnelCode", e.getPersonnelCode());
    claims.put("authorities",e.getAuthorities().stream().collect(Collectors.joining(",")));//转换为Str

    String authToken = redisService.get(AucConst.JWT_USER_REDIS_PATH + e.getUserId());
    if (StringUtils.isNotEmpty(authToken)) { // 如果token已经存在,失效之前的token
      // 删除用户id token缓存
      redisService.delete(AucConst.JWT_USER_REDIS_PATH + e.getUserId());
      // 删除 token 密钥对应缓存
      redisService.delete(AucConst.JWT_SECRET_REDIS_PATH + authToken);
    }
    //生成token
    Date authTokenExpireTime =
        Java8DateUtil.getDate(LocalDateTime.now().plusMinutes(authTokenExpireMinutes));
    authToken = generateJWTToken(authTokenExpireTime, claims, clientSecret);
    redisService.set(AucConst.JWT_USER_REDIS_PATH + e.getUserId(), authToken, authTokenExpireMinutes, TimeUnit.MINUTES);

    //刷新token继续重新生成
    Date refreshTokenExpireTime =
        Java8DateUtil.getDate(LocalDateTime.now().plusMinutes(refreshTokenExpireMinutes));
    String refreshToken = generateJWTToken(refreshTokenExpireTime, claims, clientSecret);

    LoginTokenResponseDTO token = new LoginTokenResponseDTO();
    token.setLoginInfoResponse(e);
    token.setAuthToken(authToken);
    token.setRefreshToken(refreshToken);
    return token;
  }

  /**
   * 生成jwt token
   *
   * @param expireTime 过期时间
   * @param claims
   * @return
   */
  private String generateJWTToken(Date expireTime, Map<String, Object> claims, String clientSecret) {
    String jwtSecret = StringUtils.isEmpty(clientSecret) ? String.valueOf(new SnowFlake().nextId()) : clientSecret;
    String token = Jwts.builder().addClaims(claims).setExpiration(expireTime)
        .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    redisService.set(AucConst.JWT_SECRET_REDIS_PATH + token, jwtSecret, authTokenExpireMinutes, TimeUnit.MINUTES);
    return token;
  }

  public Claims parseJWTToken(String jwtToken) {
    String jwtSecret = redisService.get(AucConst.JWT_SECRET_REDIS_PATH + jwtToken, String.class);
    if (StringUtils.isEmpty(jwtSecret)) {
      throw new AppException(ErrorCodeEnum.USER_NOT_LOGIN);
    }
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
  }

  public Claims parseJWTToken(String jwtToken, String base64Security) {
    return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security)).parseClaimsJws(jwtToken).getBody();
  }

  /**
   * 认证令牌过期后用于生成新的认证令牌
   *
   * @param refreshToken
   * @return
   */
  public RefreshTokenResponseDTO refreshToken(String refreshToken) {
    Claims claims = null;
    try {
      claims = parseJWTToken(refreshToken);
    } catch (Exception e) {
      log.error("Refresh Token failed refreshToken : {} ", refreshToken, e);
      throw new AppException(ErrorCodeEnum.USER_NOT_LOGIN);
    }
    Map<String, Object> newClaims = Maps.newHashMap();
    newClaims.put("userId", claims.get("userId"));
    newClaims.put("sourceId", claims.get("sourceId"));
    newClaims.put("sourceCode", claims.get("sourceCode"));
    newClaims.put("userName", claims.get("userName"));
    newClaims.put("personnelCode", claims.get("personnelCode"));
    newClaims.put("authorities", claims.get("authorities"));

    Date authTokenExpireTime =
        Java8DateUtil.getDate(LocalDateTime.now().plusMinutes(authTokenExpireMinutes));
    Date refreshTokenExpireTime =
        Java8DateUtil.getDate(LocalDateTime.now().plusMinutes(refreshTokenExpireMinutes));

    String newAuthToken = generateJWTToken(authTokenExpireTime, newClaims, null);
    String newRefreshToken = generateJWTToken(refreshTokenExpireTime, newClaims, null);

    RefreshTokenResponseDTO refreshTokenDTO = new RefreshTokenResponseDTO();
    refreshTokenDTO.setAuthToken(newAuthToken);
    refreshTokenDTO.setRefreshToken(newRefreshToken);
    //更新缓存的token
    redisService.set(AucConst.JWT_USER_REDIS_PATH + claims.get("userId"), newAuthToken, authTokenExpireMinutes, TimeUnit.MINUTES);
    return refreshTokenDTO;
  }

  /**
   * 生成认证实体，基于spring security authenticationToken机制
   *
   * @param authToken
   * @return
   */
  @SuppressWarnings("unchecked")
  public JWTAuthenticationToken generateAuthenticationToken(String authToken) {
    Claims claims = null;
    try {
      claims = parseJWTToken(authToken);
    } catch (ExpiredJwtException e) {
      log.error("AuthToken expire authToken: {} ", authToken);
      throw new AppException(ErrorCodeEnum.AUTH_TOKEN_EXPIRE);
    } catch (Exception e) {
      log.error("Auth failed, authToken : {}", authToken, e);
      throw new AppException(ErrorCodeEnum.USER_NOT_LOGIN);
    }
    String userId = String.valueOf(claims.get("userId"));
    String sourceId = String.valueOf(claims.get("sourceId"));
    String sourceCode = String.valueOf(claims.get("sourceCode"));
    String userName = String.valueOf(claims.get("userName"));
    String personnelCode = String.valueOf(claims.get("personnelCode"));

    return new JWTAuthenticationToken(userId, userName, personnelCode, sourceId, sourceCode,
        AuthorityUtils.createAuthorityList(String.valueOf(claims.get("authorities")).split(",")));
  }

  /**
   * 第三方的token生成方法
   * @param name
   * @param sourceId
   * @param audience
   * @param issuer
   * @param TTLMillis
   * @param base64Security
   * @return
   */
  public String createJWT(String name, Integer sourceId, String audience, String issuer,
                          long TTLMillis, String base64Security) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    // 生成签名密钥 就是一个base64加密后的字符串？
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    ObjectNode objectNode = JsonUtil.createObjectNode();
    objectNode.put("userName", name);
    objectNode.put("sourceId", sourceId);

    // 添加构成JWT的参数
    JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setIssuedAt(now) // 创建时间
        .setSubject(objectNode.toString()) // 主题，也差不多是个人的一些信息
        .setIssuer(issuer) // 发送谁
        .setAudience(audience) // 个人签名
        .signWith(signatureAlgorithm, signingKey); // 估计是第三段密钥
    // 添加Token过期时间
    if (TTLMillis >= 0) {
      // 过期时间
      long expMillis = nowMillis + TTLMillis;
      // 现在是什么时间
      Date exp = new Date(expMillis);
      // 系统时间之前的token都是不可以被承认的
      builder.setExpiration(exp).setNotBefore(now);
    }
    // 生成JWT
    return builder.compact();
  }
}
