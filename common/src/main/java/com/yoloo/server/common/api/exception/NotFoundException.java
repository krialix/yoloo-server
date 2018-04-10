package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ServiceException {

  public NotFoundException(String applicationErrorCode) {
    super(applicationErrorCode, HttpStatus.NOT_FOUND);
  }
}
