package com.auc.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoginTypeEnum {
  SYSTEM("SYSTEM", "系统登录"),
  MOBILE("MOBILE", "手机号码登录"),
  DOMAIN("DOMAIN", "域登录");

  private String code;
  private String name;

  LoginTypeEnum(String code, String name) {
    this.code = code;
    this.name = name;
  }

  @JsonValue
  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "{\"code\":\"" + code + "\"" +
        ", \"name\":\"" + name + "\"}";
  }
}
