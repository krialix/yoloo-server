package com.yoloo.spring.autoconfiguration.id.generator;

public interface IdFactory {

  @FunctionalInterface
  interface LongIdGenerator extends IdFactory {

    long generateId();
  }

  @FunctionalInterface
  interface StringIdGenerator extends IdFactory {

    String generateId();
  }
}
