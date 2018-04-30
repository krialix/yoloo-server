package com.yoloo.server.common.util;

import com.yoloo.server.common.api.exception.ConflictException;

public class ServiceExceptions {

  private ServiceExceptions() {}

  public static void checkConflict(boolean expression, String message) {
    if (!expression) {
      throw new ConflictException(message);
    }
  }
}
