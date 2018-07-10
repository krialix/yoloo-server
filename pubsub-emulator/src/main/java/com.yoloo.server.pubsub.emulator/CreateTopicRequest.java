package com.yoloo.server.pubsub.emulator;

public class CreateTopicRequest {
  private String topicName;

  public CreateTopicRequest() {}

  public String getTopicName() {
    return topicName;
  }

  @Override
  public String toString() {
    return "CreateTopicRequest{" +
        "topicName='" + topicName + '\'' +
        '}';
  }
}
