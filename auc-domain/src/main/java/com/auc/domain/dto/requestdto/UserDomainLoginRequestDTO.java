package com.auc.domain.dto.requestdto;


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
 * 域登录DTO
 * @author zhangqi
 */

@Data
@ApiModel("用户域登录信息实体")
public class UserDomainLoginRequestDTO extends LoginBaseRequestDTO {

  private static final long serialVersionUID = 2848176419608122970L;


  @ApiModelProperty(value = "域编码(GOMEKG[国美控股],GOMEDQ[国美电器],GOMEHLW[国美互联网])", required = true)
  @NotNull(message = ValidateMessage.DOMAIN_CODE_NOT_NULL)
  private DomainEnum domainCode;


  @ApiModelProperty(value = "域账号", required = true)
  @NotBlank(message = ValidateMessage.USER_NAME_NOT_BLANK)
  private String userName;


  @ApiModelProperty(value = "域密码", required = true)
  @NotBlank(message = ValidateMessage.PASSWORD_NOT_BLANK)
  private String password;

}
