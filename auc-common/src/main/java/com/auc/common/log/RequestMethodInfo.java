package com.auc.common.log;

import java.io.Serializable;
import java.util.Map;

public class RequestMethodInfo implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8511709612459995137L;

  private String className;

  private String methodName;

  private String requestMapping;

  /**
   * 方法的名称加参数信息 example:
   * public void method(String name, Object value) {} 返回 值是 method(String name, Object value)
   */
  private String methodString;

  /**
   * request parameter 的键值对 map
   */
  private Map<String, String> requestParams;

  /**
   * 除去request 和 response 的参数名称(如果取不到就用参数类型)和值 的map，值为json格式
   */
  private Map<String, String> otherParams;

  /**
   * 获取请求头参数
   */
  private Map<String, String> requestHeaderParams;


  private String type;

  private String ip;

  private boolean doInfoLog = true;

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getRequestMapping() {
    return requestMapping;
  }

  public void setRequestMapping(String requestMapping) {
    this.requestMapping = requestMapping;
  }

  public Map<String, String> getRequestParams() {
    return requestParams;
  }

  public void setRequestParams(Map<String, String> requestParams) {
    this.requestParams = requestParams;
  }

  public Map<String, String> getOtherParams() {
    return otherParams;
  }

  public void setOtherParams(Map<String, String> otherParams) {
    this.otherParams = otherParams;
  }


  public String getMethodString() {
    return methodString;
  }

  public void setMethodString(String methodString) {
    this.methodString = methodString;
  }

  @Override
  public String toString() {
    return "RequestMethodInfo [className=" + className + ", methodName=" + methodName
        + ", requestMapping="
        + requestMapping + ", requestParams=" + requestParams + ", otherParams=" + otherParams
        + "]";
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public boolean isDoInfoLog() {
    return doInfoLog;
  }

  public void setDoInfoLog(boolean doInfoLog) {
    this.doInfoLog = doInfoLog;
  }

  public Map<String, String> getRequestHeaderParams() {
    return requestHeaderParams;
  }

  public void setRequestHeaderParams(Map<String, String> requestHeaderParams) {
    this.requestHeaderParams = requestHeaderParams;
  }
}