package com.auc.domain.dto.responsedto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录成功令牌
 * 
 * @author zhangqi
 *
 */
@Data
@ApiModel(value = "登录成功dto")
public class LoginTokenResponseDTO<E extends LoginInfoResponseDTO> implements Serializable {

  private static final long serialVersionUID = 940644291147800571L;

  /**
   * 登录用户信息
   */
  @ApiModelProperty(value = "登录用户信息")
  private E loginInfoResponse;

  /**
   * 认证令牌
   */
  @ApiModelProperty(value = "认证令牌")
  private String authToken;

  /**
   * 刷新令牌
   */
  @ApiModelProperty(value = "刷新令牌")
  private String refreshToken;

}
