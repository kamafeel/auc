package com.auc.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 客户端枚举对应auc_re_client表
 */
public enum ClientEnum {
  AUC("AUC", "AUC"),
  MEETING_UNION("MEETING_UNION", "视频会议"),
  HUMAN_CAPITAL("HUMAN_CAPITAL", "人资"),
  DQ_EMS("DQ_EMS", "电器费控"),
  DQ_OA("DQ_OA", "电器OA"),
  HLW_OA("HLW_OA", "互联网OA"),
  HLW_EMS("HLW_EMS", "互联网费控"),
  BIG_DATA("BIG_DATA", "大数据应用平台"),
  CBOARD("CBOARD", "BI数据分析系统"),
  SENSORS("SENSORS", "神策流量分析系统"),
  DP("DP", "大屏系统"),
  HUKING("HUKING", "虎鲸");

  private String code;
  private String name;

  ClientEnum(String code, String name) {
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
