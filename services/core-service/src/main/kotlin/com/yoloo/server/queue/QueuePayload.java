package com.yoloo.server.queue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public final class QueuePayload {
  private final String type;
  private final Payload payload;

  @JsonCreator
  QueuePayload(String type, Payload payload) {
    this.type = type;
    this.payload = payload;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public String getType() {
    return type;
  }

  public Payload getPayload() {
    return payload;
  }

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
      property = "type")
  @JsonSubTypes({
    @JsonSubTypes.Type(value = Save.class, name = Payload.TYPE_SAVE),
    @JsonSubTypes.Type(value = Delete.class, name = Payload.TYPE_DELETE)
  })
  interface Payload {
    String TYPE_SAVE = "save";
    String TYPE_DELETE = "delete";
  }

  public static class Save implements Payload {
    private final Object data;

    @JsonCreator
    public Save(Object data) {
      this.data = data;
    }

    public Object getData() {
      return data;
    }
  }

  public static class Delete implements Payload {
    private final String urlSafeString;

    public String getUrlSafeString() {
      return urlSafeString;
    }

    @JsonCreator
    public Delete(String urlSafeString) {
      this.urlSafeString = urlSafeString;
    }
  }

  public static final class Builder {
    private String type;
    private Payload payload;

    private Builder() {}

    public Builder save() {
      this.type = Payload.TYPE_SAVE;
      return this;
    }

    public Builder delete() {
      this.type = Payload.TYPE_DELETE;
      return this;
    }

    public Builder payload(Payload payload) {
      this.payload = payload;
      return this;
    }

    public QueuePayload build() {
      return new QueuePayload(type, payload);
    }
  }
}
