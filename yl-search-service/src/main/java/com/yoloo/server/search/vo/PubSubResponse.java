package com.yoloo.server.search.vo;

public class PubSubResponse {
  private Message message;
  private String subscription;

  private PubSubResponse() {}

  public Message getMessage() {
    return message;
  }

  public String getSubscription() {
    return subscription;
  }
}
