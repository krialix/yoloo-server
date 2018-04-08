package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

  private final String applicationErrorCode;
  private final HttpStatus httpStatus;

  public ServiceException(HttpStatus httpStatus) {
    this(null, httpStatus);
  }

  public ServiceException(String applicationErrorCode, HttpStatus httpStatus) {
    this.applicationErrorCode = applicationErrorCode;
    this.httpStatus = httpStatus;
  }

  public String getApplicationErrorCode() {
    return applicationErrorCode;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
