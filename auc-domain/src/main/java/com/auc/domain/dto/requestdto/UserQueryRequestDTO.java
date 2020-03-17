package com.auc.domain.dto.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;
import java.util.List;
import lombok.Data;

/**
 * 用户信息查询
 *
 * @author zhangqi
 */
@ApiModel("用户信息查询")
@Data
public class UserQueryRequestDTO extends QueryPageParam {

  @ApiModelProperty(value = "数据源id")
  private Integer sourceId;

  @ApiModelProperty(value = "登录名")
  private String userName;

  @ApiModelProperty(hidden = true)
  private Collection<Integer> userId;

  @ApiModelProperty(value = "真实姓名")
  private String realName;

  @ApiModelProperty(value = "员工编码")
  private String personnelCode;

}
