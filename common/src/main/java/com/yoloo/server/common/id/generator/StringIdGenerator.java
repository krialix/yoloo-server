package com.yoloo.server.common.id.generator;

@FunctionalInterface
public interface StringIdGenerator extends IdGenerator {

  String generateId();
}
