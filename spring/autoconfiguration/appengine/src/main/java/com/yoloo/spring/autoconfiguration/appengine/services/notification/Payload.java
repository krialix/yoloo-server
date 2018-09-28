package com.yoloo.spring.autoconfiguration.appengine.services.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public final class Payload {
  private final String type;
  private final String fcmToken;
  private final String topic;
  private final Map<String, String> data;

  private Payload(Builder builder) {
    this(builder.type, builder.fcmToken, builder.topic, builder.data);
  }

  @SuppressWarnings("WeakerAccess")
  @JsonCreator
  Payload(
      @JsonProperty final String type,
      @JsonProperty final String fcmToken,
      @JsonProperty final String topic,
      @JsonProperty Map<String, String> data) {
    this.type = type;
    this.fcmToken = fcmToken;
    this.topic = topic;
    this.data = data;
  }

  public static Builder newBuilder(String type) {
    return new Builder(type);
  }

  public String getType() {
    return type;
  }

  public String getFcmToken() {
    return fcmToken;
  }

  public String getTopic() {
    return topic;
  }

  public Map<String, String> getData() {
    return data;
  }

  public static final class Builder {
    private String type;
    private String fcmToken;
    private String topic;
    private Map<String, String> data;

    private Builder(String type) {
      this.data = new HashMap<>();
      this.type = type;
    }

    public Builder fcmToken(String fcmToken) {
      this.fcmToken = fcmToken;
      return this;
    }

    public Builder topic(String topic) {
      this.topic = topic;
      return this;
    }

    public Builder addData(String key, String value) {
      data.putIfAbsent(key, value);
      return this;
    }

    public Payload build() {
      return new Payload(this);
    }
  }
}
