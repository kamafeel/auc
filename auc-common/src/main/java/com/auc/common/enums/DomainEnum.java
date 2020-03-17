package com.auc.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 域类型 和 auc_re_domain 保持对应
 * @author zhangqi
 */
public enum DomainEnum {
  GOMEDQ("GOMEDQ", "国美电器"),
  //GOMEHLW("GOMEHLW", "国美互联网"),
  GOMEKG("GOMEKG", "国美控股");

  private String code;
  private String name;

  DomainEnum(String code, String name) {
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
