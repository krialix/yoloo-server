package com.yoloo.server.common.util.id;

import com.fasterxml.uuid.Generators;
import org.springframework.stereotype.Component;

public class TimestampUUIDGenerator implements StringIdGenerator {

  public TimestampUUIDGenerator() {}

  @Override
  public String generateId() {
    return Generators.timeBasedGenerator().generate().toString();
  }
}
