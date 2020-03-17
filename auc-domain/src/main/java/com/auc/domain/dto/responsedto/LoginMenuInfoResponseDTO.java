package com.auc.domain.dto.responsedto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "菜单信息dto")
public class LoginMenuInfoResponseDTO implements Serializable {
  private static final long serialVersionUID = -5213434609760490395L;
  @ApiModelProperty(value = "菜单id")
  private String id;
  @ApiModelProperty(value = "菜单父id")
  private Long parentId;
  @ApiModelProperty(value = "菜单名称")
  private String menuName;
  @ApiModelProperty(value = "菜单地址")
  private String menuUrl;

}
