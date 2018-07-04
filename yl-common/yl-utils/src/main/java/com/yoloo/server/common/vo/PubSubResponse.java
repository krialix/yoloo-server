package com.yoloo.server.common.vo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class PubSubResponse {

  private PubSubMessage message;
  private String subscription;

  private PubSubResponse() {}

  public static PubSubResponse from(ObjectMapper mapper, InputStream inputStream)
      throws IOException {
    return mapper.readValue(inputStream, PubSubResponse.class);
  }

  public PubSubMessage getMessage() {
    return message;
  }

  public String getSubscription() {
    return subscription;
  }
}
