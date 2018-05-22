package com.yoloo.server.api.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ServiceException {

  public ServiceUnavailableException(String message, Object... args) {
    super(HttpStatus.SERVICE_UNAVAILABLE, message, args);
  }
}
