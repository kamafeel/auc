package com.auc.common.enums;


import java.util.Arrays;

/**
 * @author zhangqi
 * @Description:日志类型
 */

public enum LogTypeEnum {

  /**
   * 正常
   */
  NORMAL(100, "正常", "#NORMAL#"),
  AUTH_DOMAIN(110, "域登录", "auth/domainLogin"),
  AUTH_MOBILE(111, "手机号登录", "auth/mobilePhoneLogin"),
  USER_DISABLE(120, "禁用用户", "user/disable"),
  USER_ENABLE(121, "启用用户", "user/enable"),
  USER_PASSWORD(122, "修改密码", "user/updateOperationPassword"),

  /**
   * 错误
   */
  ERROR(999, "错误", "#ERROR#");

  private Integer code;
  private String name;
  private String path;

  public static LogTypeEnum valueOfPath(String path) {
    return Arrays.stream(LogTypeEnum.values()).filter(e->path.contains(e.getPath())).findAny().orElse(LogTypeEnum.NORMAL);
  }

  LogTypeEnum(Integer code, String name, String path) {
    this.code = code;
    this.name = name;
    this.path = path;
  }

  public Integer getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }
}
