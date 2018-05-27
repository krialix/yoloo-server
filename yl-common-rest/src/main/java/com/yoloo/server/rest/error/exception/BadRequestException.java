package com.yoloo.server.rest.error.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ServiceException {

  public BadRequestException(String message, Object... args) {
    super(HttpStatus.BAD_REQUEST, message, args);
  }
}
