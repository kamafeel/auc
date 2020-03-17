package com.auc.common;

import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.CommonsUtils;
import java.util.List;
import com.google.common.base.Strings;

public class Assert {
  public static void isNotNull(Object obj, ErrorCodeEnum errorCode) {
    isNotNull(obj, errorCode, null);
  }

  public static void isNotNull(Object obj, ErrorCodeEnum errorCode, String message) throws AppException {
    if (obj == null) {
      throw new AppException(errorCode, message);
    }
  }

  public static void isNull(Object obj, ErrorCodeEnum errorCode) {
    isNull(obj, errorCode, null);
  }

  public static void isNull(Object obj, ErrorCodeEnum errorCode, String message) throws AppException {
    if (obj != null) {
      throw new AppException(errorCode, message);
    }
  }

  public static void isTrue(boolean condition, ErrorCodeEnum errorCode) {
    isTrue(condition, errorCode, null);
  }

  /**
   *  condition is false throw AppException
   * @param condition
   * @param errorCode
   * @param message
   * @throws AppException
   */
  public static void isTrue(boolean condition, ErrorCodeEnum errorCode, String message)
      throws AppException {
    if (!condition) {
      throw new AppException(errorCode, message);
    }
  }

  /**
   *  condition is true throw AppException
   * @param condition
   * @param errorCode
   * @param message
   * @throws AppException
   */
  public static void isFasle(boolean condition, ErrorCodeEnum errorCode, String message)
      throws AppException {
    isTrue(!condition, errorCode, message);
  }

  public static void isEquals(Object obj1, Object obj2, ErrorCodeEnum code) throws AppException {
    isEquals(obj1, obj2, code, null);
  }

  public static void isEquals(Object obj1, Object obj2, ErrorCodeEnum code, String message)
      throws AppException {
    if (obj1 == obj2) {
      return;
    }
    if (obj1 != null && obj1.equals(obj2)) {
      return;
    }
    throw new AppException(code, message);
  }

  public static void isNotEmpty(String str, ErrorCodeEnum code) {
    isNotEmpty(str, code, null);
  }

  public static void isNotEmpty(String str, ErrorCodeEnum code, String message) {
    if (Strings.isNullOrEmpty(str)) {
      throw new AppException(code, message);
    }
  }

  public static void isNumeric(String str, ErrorCodeEnum code) {
    isNumeric(str, code, null);
  }

  /**
   * <pre>
   * 如果非空，抛异常
   * @param list
   * @param code
   * @param message
   */
  public static void isListEmpty(List<?> list, ErrorCodeEnum code, String message) {
    if (list != null && list.size() > 0) {
      throw new AppException(code, message);
    }
  }

  /**
   * <pre>
   * 如果非空，抛异常
   * @param list
   * @param code
   * @param message
   */
  public static void isListEmpty(List<?> list, ErrorCodeEnum code) {
    isListEmpty(list, code, null);
  }

  /**
   * <pre>
   * 如果空，抛异常
   * @param list
   * @param code
   * @param message
   */
  public static void isListNotEmpty(List<?> list, ErrorCodeEnum code, String message) {
    if (list == null || list.isEmpty()) {
      throw new AppException(code, message);
    }
  }

  /**
   * <pre>
   * 如果空，抛异常
   * @param list
   * @param code
   * @param message
   */
  public static void isListNotEmpty(List<?> list, ErrorCodeEnum code) {
    isListNotEmpty(list, code, null);
  }

  /**
   * str必须是自然数和0
   *
   * @param str 非负整数
   * @param code
   * @param args
   */
  public static void isNumeric(String str, ErrorCodeEnum code, String message) {
    if (Strings.isNullOrEmpty(str) || !CommonsUtils.isNumeric(str)) {
      throw new AppException(code, message);
    }
  }
}
