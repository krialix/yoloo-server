package com.yoloo.server.common.vo;

public class PubSubResponse {

  private PubSubMessage message;
  private String subscription;

  private PubSubResponse() {
  }

  public PubSubMessage getMessage() {
    return message;
  }

  public String getSubscription() {
    return subscription;
  }
}
