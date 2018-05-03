package com.yoloo.server.common.util;

import com.yoloo.server.common.api.exception.ConflictException;
import com.yoloo.server.common.api.exception.NotFoundException;

public class ServiceExceptions {

  private ServiceExceptions() {}

  public static void checkConflict(boolean expression, String message) {
    if (!expression) {
      throw new ConflictException(message);
    }
  }

  public static void checkNotFound(boolean expression, String message) {
    if (!expression) {
      throw new NotFoundException(message);
    }
  }

  public static void checkForbidden(boolean expression, String message) {
    if (!expression) {
      throw new NotFoundException(message);
    }
  }
}
