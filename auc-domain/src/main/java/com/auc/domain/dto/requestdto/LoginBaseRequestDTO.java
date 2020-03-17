package com.auc.domain.dto.requestdto;


import com.auc.common.constants.ValidateMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 手机号码登录DTO
 * @author zhangqi
 */

@Data
@ApiModel("登录基础对象")
public class LoginBaseRequestDTO implements Serializable {

  private static final long serialVersionUID = 2848176419608122970L;

  @ApiModelProperty(value = "第三方应用的ID", required = true)
  @NotBlank(message = ValidateMessage.CLIENT_ID_NOT_BLANK)
  private String client_id;


  @ApiModelProperty(value = "第三方应用的密钥", required = false)
  private String client_secret;

  @ApiModelProperty(value = "防止CSRF随机数", required = true)
  @NotBlank(message = ValidateMessage.STATE_NOT_BLANK)
  private String state;

  @ApiModelProperty(value = "回调URL", required = false)
  private String redirect_uri;

}
