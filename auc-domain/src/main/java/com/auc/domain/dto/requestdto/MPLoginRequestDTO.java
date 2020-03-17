package com.auc.domain.dto.requestdto;


import com.auc.common.constants.AucConst;
import com.auc.common.constants.ValidateMessage;
import com.auc.common.enums.DomainEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;


/**
 * 手机号码登录DTO
 * @author zhangqi
 */

@Data
@ApiModel("用户手机号码登录信息实体")
public class MPLoginRequestDTO extends LoginBaseRequestDTO{

  private static final long serialVersionUID = 2848176419608122970L;

  @ApiModelProperty(value = "手机号码", required = true)
  @NotBlank(message = ValidateMessage.USER_NAME_NOT_BLANK)
  @Pattern(regexp = AucConst.REGEX_MOBILE_EXACT, message = ValidateMessage.MOBILE_PHONE_INCORRECT)
  private String userName;


  @ApiModelProperty(value = "验证码", required = true)
  @NotBlank(message = ValidateMessage.PASSWORD_NOT_BLANK)
  private String password;

  @ApiModelProperty(value = "数据源,如果不传默认使用DQ电器数据源")
  private String sourceCode;

}
