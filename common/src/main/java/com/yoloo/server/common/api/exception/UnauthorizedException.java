package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServiceException {

  public UnauthorizedException(String applicationErrorCode) {
    super(applicationErrorCode, HttpStatus.UNAUTHORIZED);
  }
}
