package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ServiceException {

  public ConflictException(String applicationErrorCode) {
    super(applicationErrorCode, HttpStatus.CONFLICT);
  }
}
