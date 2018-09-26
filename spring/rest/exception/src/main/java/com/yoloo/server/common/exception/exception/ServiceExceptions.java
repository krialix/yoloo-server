package com.yoloo.server.common.exception.exception;

public class ServiceExceptions {

  private ServiceExceptions() {}

  public static void checkBadRequest(boolean expected, String message, Object... args) {
    if (!expected) {
      throw new BadRequestException(message, args);
    }
  }

  public static void checkConflict(boolean expected, String message, Object... args) {
    if (!expected) {
      throw new ConflictException(message, args);
    }
  }

  public static void checkForbidden(boolean expected, String message, Object... args) {
    if (!expected) {
      throw new ForbiddenException(message, args);
    }
  }

  public static void checkNotFound(boolean expected, String message, Object... args) {
    if (!expected) {
      throw new NotFoundException(message, args);
    }
  }
}
