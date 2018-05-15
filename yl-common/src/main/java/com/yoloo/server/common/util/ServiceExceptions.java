package com.yoloo.server.common.util;

import com.yoloo.server.common.api.exception.BadRequestException;
import com.yoloo.server.common.api.exception.ConflictException;
import com.yoloo.server.common.api.exception.NotFoundException;

public class ServiceExceptions {

  private ServiceExceptions() {}

  public static void checkBadRequest(boolean expression, String message, Object... args) {
    if (!expression) {
      throw new BadRequestException(message, args);
    }
  }

  public static void checkConflict(boolean expression, String message, Object... args) {
    if (!expression) {
      throw new ConflictException(message, args);
    }
  }

  public static void checkForbidden(boolean expression, String message, Object... args) {
    if (!expression) {
      throw new NotFoundException(message, args);
    }
  }

  public static void checkNotFound(boolean expression, String message, Object... args) {
    if (!expression) {
      throw new NotFoundException(message, args);
    }
  }
}
