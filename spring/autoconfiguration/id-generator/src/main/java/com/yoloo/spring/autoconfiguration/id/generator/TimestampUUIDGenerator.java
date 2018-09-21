package com.yoloo.spring.autoconfiguration.id.generator;

import com.fasterxml.uuid.Generators;

public class TimestampUUIDGenerator implements IdFactory.StringIdGenerator {

  @Override
  public String generateId() {
    return Generators.timeBasedGenerator().generate().toString();
  }
}
