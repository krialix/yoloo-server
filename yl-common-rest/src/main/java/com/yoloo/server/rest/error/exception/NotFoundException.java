package com.yoloo.server.rest.error.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ServiceException {

  public NotFoundException(String message, Object... args) {
    super(HttpStatus.NOT_FOUND, message, args);
  }
}
