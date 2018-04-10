package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ServiceException {

  public BadRequestException(String applicationErrorCode) {
    super(applicationErrorCode, HttpStatus.BAD_REQUEST);
  }
}
