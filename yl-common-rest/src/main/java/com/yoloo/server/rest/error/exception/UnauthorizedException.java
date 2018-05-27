package com.yoloo.server.rest.error.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServiceException {

  public UnauthorizedException(String message, Object... args) {
    super(HttpStatus.UNAUTHORIZED, message, args);
  }
}
