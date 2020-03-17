package com.auc.domain.dto.requestdto;

import com.auc.common.enums.LogTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * 日志信息查询
 *
 * @author zhangqi
 */
@ApiModel("日志信息查询")
@Data
public class ClientQueryRequestDTO extends QueryPageParam {

  @ApiModelProperty(notes = "客户端ID（模糊匹配）")
  private String clientId;

  @ApiModelProperty(notes = "客户端名称（模糊匹配）")
  private String clientName;

  @ApiModelProperty(notes = "是否在跳转列表显示（0 显示， 1 不显示）")
  private Integer status;

  @ApiModelProperty(notes = "登录类型（0：域账号登录，1：系统账号登录）")
  private Integer loginType;

}
