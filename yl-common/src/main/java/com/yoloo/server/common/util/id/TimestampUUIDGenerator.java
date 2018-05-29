package com.yoloo.server.common.util.id;

import com.fasterxml.uuid.Generators;

public class TimestampUUIDGenerator implements StringIdGenerator {

  public TimestampUUIDGenerator() {
  }

  @Override
  public String generateId() {
    return Generators.timeBasedGenerator().generate().toString();
  }
}
