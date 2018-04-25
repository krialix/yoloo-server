package com.yoloo.server.common.id;

@FunctionalInterface
public interface LongIdGenerator extends IdGenerator {

  long generateId();
}
