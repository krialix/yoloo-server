package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ServiceException {

  public ServiceUnavailableException(String applicationErrorCode) {
    super(applicationErrorCode, HttpStatus.SERVICE_UNAVAILABLE);
  }
}
