package com.yoloo.server.queue;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class QueuePayload {
  private final Type type;
  private final Object payload;

  @JsonCreator
  QueuePayload(Type type, Object payload) {
    this.type = type;
    this.payload = payload;
  }

  public static QueuePayload save(Object payload) {
    return new QueuePayload(Type.SAVE, payload);
  }

  public static QueuePayload delete(String urlSafeString) {
    return new QueuePayload(Type.DELETE, urlSafeString);
  }

  public Type getType() {
    return type;
  }

  public Object getPayload() {
    return payload;
  }

  public enum Type {
    SAVE,
    DELETE
  }
}
