package com.auc.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zhangqi73
 * 运行参数变量
 */
public enum VariableTypeEnum {

  SYNC(1, "数据同步"),
  CLEAR(2, "历史数据转移");

  private Integer type;

  private String typeName;

  VariableTypeEnum(Integer type, String typeName) {
    this.type = type;
    this.typeName = typeName;
  }

  @JsonValue
  public Integer getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }

  @Override
  public String toString() {
    return "{\"type\":\"" + type + "\"" +
        ", \"typeName\":\"" + typeName + "\"}";
  }

}
