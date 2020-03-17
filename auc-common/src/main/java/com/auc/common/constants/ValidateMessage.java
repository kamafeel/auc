package com.auc.common.constants;

/**
 * 校验异常信息
 * 
 * @author zhangqi
 *
 */
public class ValidateMessage {

  /**
   * 可配合validationMessage.properties使用
   */
  /** 登录密码不能为空 */
  public static final String PASSWORD_NOT_BLANK = "{password.notBlank}";
  /** 域用户名不能为空 */
  public static final String USER_NAME_NOT_BLANK = "{userName.notBlank}";
  /** 域编码不能为空 */
  public static final String DOMAIN_CODE_NOT_NULL = "{domainCode.notBlank}";
  /** 手机号不能为空 */
  public static final String MOBILE_PHONE_NOT_BLANK = "{mobilePhone.notBlank}";
  /** 手机号格式错误 */
  public static final String MOBILE_PHONE_INCORRECT = "{mobilePhone.incorrect}";
  /** 短信类型不能为空 */
  public static final String SMS_TYPE_NOT_BLANK = "{smsType.notBlank}";
  /** 第三方应用的 ID不能为空 */
  public static final String CLIENT_ID_NOT_BLANK = "{client.id.notBlank}";
  /** state不能为空 */
  public static final String STATE_NOT_BLANK = "{state.notBlank}";

  /** 传入参数为空 */
  public static final String PARAMETER_IS_NULL = "{common.parameterIsNull}";
}
