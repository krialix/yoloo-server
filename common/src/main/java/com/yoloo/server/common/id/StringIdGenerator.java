package com.yoloo.server.common.id;

@FunctionalInterface
public interface StringIdGenerator extends IdGenerator {

  String generateId();
}
