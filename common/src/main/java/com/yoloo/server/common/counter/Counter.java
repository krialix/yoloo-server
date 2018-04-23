package com.yoloo.server.common.counter;

import com.googlecode.cqengine.attribute.Attribute;

import java.util.Objects;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

public class Counter {

  public static final Attribute<Counter, String> COUNTER_KEY = attribute("key", Counter::getKey);

  private final String key;
  private final long value;

  public Counter(String key, long value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public long getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Counter counter = (Counter) o;
    return value == counter.value && Objects.equals(key, counter.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }
}
