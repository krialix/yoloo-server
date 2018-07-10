package com.yoloo.server.pubsub.emulator;

public class CreateSubscriptionRequest {
  String topicName;
  String subscriptionName;

  public String getTopicName() {
    return topicName;
  }

  public String getSubscriptionName() {
    return subscriptionName;
  }

  public CreateSubscriptionRequest() {}
}
