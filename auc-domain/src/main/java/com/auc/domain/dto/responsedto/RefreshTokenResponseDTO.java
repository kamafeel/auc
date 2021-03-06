package com.auc.domain.dto.responsedto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 刷新成功令牌
 * 
 * @author weibo
 *
 */
@Data
@ApiModel(value = "刷新成功令牌")
public class RefreshTokenResponseDTO implements Serializable {

  private static final long serialVersionUID = 940644291147800571L;

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
