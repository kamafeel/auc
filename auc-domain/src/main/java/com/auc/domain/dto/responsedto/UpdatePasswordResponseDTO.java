package com.auc.domain.dto.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;


/**
 * @author  zhangchangzhong
 * @create  2019/12/24 14:53
 * @desc
 **/
@SuppressWarnings("deprecation")
@Data
@ApiModel(value = "修改密码的返回体")
public class UpdatePasswordResponseDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "状态")
  private Boolean status;
  @ApiModelProperty(value = "修改成功消息")
  private String successMsg;
  @ApiModelProperty(value = "修改失败消息")
  private String errorMsg;

  public UpdatePasswordResponseDTO() {
  }

  public UpdatePasswordResponseDTO(Boolean status, String successMsg, String errorMsg) {
    this.status = status;
    this.successMsg = successMsg;
    this.errorMsg = errorMsg;
  }
}
