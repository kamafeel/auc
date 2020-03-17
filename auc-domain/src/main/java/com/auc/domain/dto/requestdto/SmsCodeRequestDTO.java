package com.auc.domain.dto.requestdto;

import com.auc.common.constants.AucConst;
import com.auc.common.constants.ValidateMessage;
import com.auc.common.enums.SMSEnum;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取手机号短信码
 * 
 * @author zhangqi
 */
@ApiModel("获取手机号短信码")
@Data
public class SmsCodeRequestDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "手机号", required = true)
  @NotBlank(message = ValidateMessage.MOBILE_PHONE_NOT_BLANK)
  @Pattern(regexp = AucConst.REGEX_MOBILE_EXACT, message = ValidateMessage.MOBILE_PHONE_INCORRECT)
  private String mobilePhone;

  @ApiModelProperty(value = "登录密码(SMS_LOGIN_PASSWORD),密码重置(SMS_RESET_PWD)", required = true)
  @NotNull(message = ValidateMessage.SMS_TYPE_NOT_BLANK)
  private SMSEnum smsType;
}
