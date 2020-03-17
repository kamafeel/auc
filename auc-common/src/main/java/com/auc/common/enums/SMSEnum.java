package com.auc.common.enums;

/**
 * 短信类型枚举
 * @author zhangqi
 *
 */
public enum SMSEnum {

  SMS_LOGIN_PASSWORD("1" , "登录验证码" ,
      "短信验证码是{smsCode}，此验证码60秒内有效，如非本人操作，请忽略本条短信。"),

  SMS_RESET_PWD("2" , "密码重置验证码" ,
      "短信验证码是{smsCode}，此验证码60秒内有效，如非本人操作，请忽略本条短信。");

  private String code;
  private String title;
  private String content;

  private SMSEnum(String code, String title, String content) {
    this.code = code;
    this.setTitle(title);
    this.setContent(content);
  }

  public static SMSEnum getSms(String code) {
    try {
      for (SMSEnum e : SMSEnum.values()) {
        if (e.name().equalsIgnoreCase(code)) {
          return e;
        }
      }
    } catch (Exception e) {
    }
    return null;
  }

  public String getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public static void main(String[] args) {
    for (SMSEnum e : SMSEnum.values()) {
      System.out.println("insert into sms values ('" + e.name() + "');");
    }
  }
}
