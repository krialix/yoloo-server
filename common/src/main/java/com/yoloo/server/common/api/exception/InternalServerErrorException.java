package com.yoloo.server.common.api.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ServiceException {

  public InternalServerErrorException(String applicationErrorCode) {
    super(applicationErrorCode, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
