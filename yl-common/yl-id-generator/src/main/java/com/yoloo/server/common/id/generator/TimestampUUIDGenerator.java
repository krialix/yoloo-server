package com.yoloo.server.common.id.generator;

import com.fasterxml.uuid.Generators;

public class TimestampUUIDGenerator implements StringIdGenerator {

  public TimestampUUIDGenerator() {
  }

  @Override
  public String generateId() {
    return Generators.timeBasedGenerator().generate().toString();
  }
}
