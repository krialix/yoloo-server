package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public abstract class ServiceException extends RuntimeException {

  private final HttpStatus httpStatus;

  public ServiceException(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public ServiceException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
