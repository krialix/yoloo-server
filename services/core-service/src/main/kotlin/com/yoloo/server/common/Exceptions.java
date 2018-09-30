package com.yoloo.server.common;

import org.zalando.problem.Problem;
import org.zalando.problem.StatusType;

public final class Exceptions {

  private Exceptions() {}

  public static void checkException(boolean expected, StatusType statusType, String detail) {
    if (!expected) {
      throw Problem.valueOf(statusType, detail);
    }
  }
}
