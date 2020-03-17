package com.auc.common.enums;

/**
 * 错误码规则为四位纯数字 
 * 1.公共模块 
 * 2.登录模块
 * 
 * @author zhangqi
 *
 */
public enum ErrorCodeEnum {

  /**
   * (保留码) 表示未知异常
   */
  UNEXCEPTED(9999),

  /* 公共模块错误码 */

  /**
   * 成功
   */
  SUCCESS(1000),
  /**
   * 用户未登录
   */
  USER_NOT_LOGIN(1001),
  /**
   * 认证令牌过期
   */
  AUTH_TOKEN_EXPIRE(1002),
  /**
   * 非法参数
   */
  ILLEGAL_PARAMETER(1003),
  /**
   * 权限不足
   */
  ACCESS_DENIED(1004),
  /**
   * 结果为空
   */
  RESULT_EMPTY(1005),

  /**
   * 操作数据库失败
   */
  FAIL_DATABASE(1006),

  /**
   * 操作数据库失败数据主键冲突
   */
  FAIL_DATABASE_DUPLICATE_KEY(1007),

  /**
   * 域登录服务器超时
   */
  DOMAIN_SERVER_ERROR(1008),

  /**
   * 获取分布式锁失败/超时
   */
  LOCK_FAIL(1009),


  /* 登录模块 */

  /**
   * 用户不存在
   */
  USER_NOT_EXISTS(2001),
  /**
   * 验证码不正确
   */
  IMG_CODE_INCORRECT(2002),

  /**
   * 未指定数据源
   */
  UNSPECIFIED_DATA_SOURCE(2003),
  /**
   * 密码错误
   */
  PASSWORD_IS_WRONG(2004),
  /**
   * 手机验证码错误
   */
  PHONE_CODE_IS_WRONG(2005),
  /**
   * 域编码为空
   */
  DOMAIN_CODE_IS_EMPTY(2006),
  /**
   * 手机号为空
   */
  MOBILE_PHONE_IS_EMPTY(2007),

  /**
   * 短信发送频率过高
   */
  SMS_FREQUENCY_IS_HIGH(2008),

  /**
   * 单位时间内用户名密码错误过多
   */
  PW_ERROR_TOO_MUCH(2009),

  /**
   * 验证码为空
   */
  IMG_CODE_IS_EMPTY(2010),

  /**
   * 用户类错误异常
   **/
  USER_ERROR(2020),
  /*
  *密码太过简单,必须包含特殊字符、数字、字母的组合
  * */
  PASSWORD_NOT_DISQUALIFIED(2021),

  /*
  *修改密码的用户数据错误
  * */
  UPDATEPASSWORD_USERERRO(2022),

  /* 文件处理模块 */
  FAIL_FILE(3001);


  private int value;

  private ErrorCodeEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
