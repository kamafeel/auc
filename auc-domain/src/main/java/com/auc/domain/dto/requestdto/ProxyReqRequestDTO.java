package com.auc.domain.dto.requestdto;


import com.auc.common.constants.ValidateMessage;
import com.auc.common.enums.DomainEnum;
import com.auc.common.enums.LoginTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;


/**
 * 外部登录DTO
 * 代理模式,token生成（可选）
 * @author zhangqi
 */

@Data
@ApiModel("用户登录信息实体(代理)")
public class ProxyReqRequestDTO extends LoginBaseRequestDTO {

  private static final long serialVersionUID = 2848176419608122970L;


  @ApiModelProperty(value = "域编码(GOMEKG[国美控股],GOMEDQ[国美电器],GOMEHLW[国美互联网])", required = false)
  @NotNull(message = ValidateMessage.DOMAIN_CODE_NOT_NULL)
  private DomainEnum domainCode;

  @ApiModelProperty(value = "数据源编码", required = false)
  private String sourceCode;

  @ApiModelProperty(value = "登录类型(SYSTEM[应用系统登录],MOBILE[手机号码登录(请先调用获取验证码接口)],DOMAIN[域登录])", required = true)
  private LoginTypeEnum loginTypeEnum;


  @ApiModelProperty(value = "域账号/应用系统账号/手机号", required = true)
  @NotBlank(message = ValidateMessage.USER_NAME_NOT_BLANK)
  private String userName;


  @ApiModelProperty(value = "密码/手机验证码", required = true)
  @NotBlank(message = ValidateMessage.PASSWORD_NOT_BLANK)
  private String password;

  @ApiModelProperty(value = "是否需要创建AUC系统token", required = true)
  private boolean isCreateToken;

}
