package com.yoloo.server.search.event;

import java.util.HashMap;
import java.util.Map;

public class Event {
  private Metadata metadata;
  private Map<String, Object> payload;

  private Event() {}

  private Event(Builder builder) {
    this.metadata = builder.metadata;
    this.payload = builder.payload;
  }

  public static Builder newBuilder(Metadata metadata) {
    return new Builder(metadata);
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public Map<String, Object> getPayload() {
    return payload;
  }

  @Override
  public String toString() {
    return "Event{" + "metadata=" + metadata + ", payload=" + payload + '}';
  }

  public static final class Builder {
    Metadata metadata;
    Map<String, Object> payload;

    private Builder(Metadata metadata) {
      this.metadata = metadata;
      this.payload = new HashMap<>();
    }

    public Builder addData(String key, Object value) {
      this.payload.putIfAbsent(key, value);
      return this;
    }

    public Event build() {
      return new Event(this);
    }
  }

  public static final class Metadata {
    private EventType type;

    private Metadata() {}

    private Metadata(EventType type) {
      this.type = type;
    }

    public static Metadata of(EventType type) {
      return new Metadata(type);
    }

    public EventType getType() {
      return type;
    }

    @Override
    public String toString() {
      return "Metadata{" + "type=" + type + '}';
    }
  }
}
