package com.yoloo.server.rest.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ServiceException {

  public ForbiddenException(String message, Object... args) {
    super(HttpStatus.FORBIDDEN, message, args);
  }
}
