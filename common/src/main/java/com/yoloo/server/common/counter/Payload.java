package com.yoloo.server.common.counter;

import java.util.Objects;

public class Payload {
  private final String ownerId;
  private final long value;

  public Payload(String ownerId, long value) {
    this.ownerId = ownerId;
    this.value = value;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public long getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Payload payload = (Payload) o;
    return value == payload.value && Objects.equals(ownerId, payload.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ownerId, value);
  }
}
