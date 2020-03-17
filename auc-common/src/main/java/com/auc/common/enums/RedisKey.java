package com.auc.common.enums;

public enum RedisKey {
  /**
   * 短信验证码
   */
  SMS_CODE(String.class),
  /**
   * 登录短信验证码
   */
  LOGIN_SMS_CODE(String.class),
  /**
   * 密码重置短信验证码
   */
  RESET_SMS_CODE(String.class),
  /**
   * 控制短信频率
   */
  SMS_FREQUENCY(String.class),

  /**
   * 用户名密码错误次数
   */
  PW_ERROR_NUM(String.class),

  /**
   * 图片验证码KEY
   */
  IMG_CODE_KEY(String.class);

  private Class<?> clazz;

  private <T> RedisKey(Class<T> clazz) {
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public <T> Class<T> getClazz() {
    return (Class<T>) clazz;
  }
}
