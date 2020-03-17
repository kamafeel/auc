package com.auc.domain.dto.requestdto;


import com.auc.common.constants.ValidateMessage;
import com.auc.common.enums.DomainEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;


/**
 * 系统登录DTO
 * @author zhangqi
 */

@Data
@ApiModel("用户系统登录信息实体")
public class UserSystemLoginRequestDTO extends LoginBaseRequestDTO {

  private static final long serialVersionUID = 2848176419608122970L;


  @ApiModelProperty(value = "数据源编码", required = true)
  @NotBlank(message = ValidateMessage.PARAMETER_IS_NULL)
  private String sourceCode;


  @ApiModelProperty(value = "app_account应用系统账号", required = true)
  @NotBlank(message = ValidateMessage.USER_NAME_NOT_BLANK)
  private String userName;


  @ApiModelProperty(value = "密码", required = true)
  @NotBlank(message = ValidateMessage.PASSWORD_NOT_BLANK)
  private String password;

}
