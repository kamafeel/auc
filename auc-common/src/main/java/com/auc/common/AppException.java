package com.auc.common;

import com.auc.common.enums.ErrorCodeEnum;
import org.apache.commons.lang3.StringUtils;

public class AppException extends RuntimeException {
  private static final long serialVersionUID = 8449738842423044010L;

  private ErrorCodeEnum code;

  public AppException(ErrorCodeEnum code) {
    this.code = code;
  }

  public AppException(ErrorCodeEnum code, String message) {
    super(message);
    this.code = code;
  }

  public ErrorCodeEnum getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    if (StringUtils.isBlank(super.getMessage())) {
      return code.toString();
    }
    return super.getMessage();
  }
}
