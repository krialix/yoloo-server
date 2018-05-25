package com.yoloo.server.common.util.id;

@FunctionalInterface
public interface StringIdGenerator extends IdGenerator {

  String generateId();
}
