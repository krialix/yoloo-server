package com.yoloo.server.common.util;

import com.fasterxml.uuid.Generators;

public class TimestampIdGenerator {

  private TimestampIdGenerator() {}

  public static String generateId() {
    return Generators.timeBasedGenerator().generate().toString().replace("-", "");
  }
}
