package com.yoloo.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.Result;
import com.yoloo.server.common.appengine.util.AppengineEnv;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

import java.util.Arrays;
import java.util.Map;

public abstract class BaseUseCase {

  private final ObjectMapper mapper;
  private final PubSubTemplate pubSubTemplate;

  protected BaseUseCase(ObjectMapper mapper, PubSubTemplate pubSubTemplate) {
    this.mapper = mapper;
    this.pubSubTemplate = pubSubTemplate;
  }

  protected void putResult(Result<?>... results) {
    if (AppengineEnv.isTest()) {
      Arrays.asList(results).forEach(Result::now);
    }
  }

  protected void publishEvent(String topic, Object item) throws JsonProcessingException {
    publishEvent(topic, item, null);
  }

  protected void publishEvent(String topic, Object item, Map<String, String> headers)
      throws JsonProcessingException {
    String json = mapper.writeValueAsString(item);
    pubSubTemplate.publish(topic, json, headers);
  }
}
