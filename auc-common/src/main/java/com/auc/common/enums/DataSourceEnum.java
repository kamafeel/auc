package com.auc.common.enums;


/**
 * 数据源枚举
 */
public enum DataSourceEnum {
  DQ("DQ", "电器"),
  HLW("HLW", "互联网"),
  KG("KG", "控股集团");

  String sourceCode;

  String sourceName;

  DataSourceEnum(String sourceCode, String sourceName) {
    this.sourceCode = sourceCode;
    this.sourceName = sourceName;
  }

  public String getSourceCode() {
    return sourceCode;
  }

  public String getSourceName() {
    return sourceName;
  }

  @Override
  public String toString() {
    return "{\"sourceCode\":\"" + sourceCode + "\", \"sourceName\":\"" + sourceName + "\"}";
  }

}
