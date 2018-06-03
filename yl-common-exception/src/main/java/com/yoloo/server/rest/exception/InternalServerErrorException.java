package com.yoloo.server.rest.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ServiceException {

  public InternalServerErrorException(String message, Object... args) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message, args);
  }
}
