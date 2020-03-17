package com.auc.domain.dto.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("账号转换查询")
@Data
public class AccountConvertRequestDTO extends QueryPageParam {

  @ApiModelProperty(notes = "数据源编码")
  private String sourceCode;

  @ApiModelProperty(notes = "转换账号对应用户名（模糊匹配）")
  private String convertLoginName;

  @ApiModelProperty(notes = "第三方系统ID")
  private String clientId;
}
