package com.yoloo.server.common.id.generator;

import com.fasterxml.uuid.Generators;
import org.springframework.stereotype.Component;

@Component("timestamp")
public class TimestampUUIDGenerator implements StringIdGenerator {

  private TimestampUUIDGenerator() {}

  @Override
  public String generateId() {
    return Generators.timeBasedGenerator().generate().toString();
  }
}
