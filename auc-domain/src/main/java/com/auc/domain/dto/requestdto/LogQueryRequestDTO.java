package com.auc.domain.dto.requestdto;

import com.auc.common.constants.AucConst;
import com.auc.common.constants.ValidateMessage;
import com.auc.common.enums.LogTypeEnum;
import com.auc.common.enums.SMSEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 日志信息查询
 *
 * @author zhangqi
 */
@ApiModel("日志信息查询")
@Data
public class LogQueryRequestDTO extends QueryPageParam {

  @ApiModelProperty(value = "日志内容")
  private String logContent;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "用户ID")
  private Collection<Integer> userId;

  @ApiModelProperty(value = "日志级别")
  private LogTypeEnum logTypeEnum;

  @ApiModelProperty(value = "开始时间")
  private Date startTime;

  @ApiModelProperty(value = "结束时间")
  private Date endTime;

  @ApiModelProperty(value = "最后一条日志的ID")
  private Long id;
}
