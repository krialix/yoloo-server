package com.yoloo.server.common.id.generator;

@FunctionalInterface
public interface LongIdGenerator extends IdGenerator {

  long generateId();
}
