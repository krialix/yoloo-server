package com.yoloo.server.common.util.id;

@FunctionalInterface
public interface LongIdGenerator extends IdGenerator {

  long generateId();
}
