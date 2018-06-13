package com.yoloo.server.common.exception.exception;

import org.springframework.http.HttpStatus;

public abstract class ServiceException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final Object[] args;

  public ServiceException(HttpStatus httpStatus, String message, Object[] args) {
    super(message);
    this.httpStatus = httpStatus;
    this.args = args;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public Object[] getArgs() {
    return args;
  }
}
