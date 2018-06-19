package com.yoloo.server.notification.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.notification.payload.NotificationPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gcp.pubsub.PubSubAdmin;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pubsub")
@RestController
public class PubSubController {

  private static final Logger LOGGER = LogManager.getLogger();

  private final PubSubAdmin pubSubAdmin;
  private final PubSubTemplate pubSubTemplate;
  private final ObjectMapper mapper;

  public PubSubController(
      PubSubAdmin pubSubAdmin, PubSubTemplate pubSubTemplate, ObjectMapper mapper) {
    this.pubSubAdmin = pubSubAdmin;
    this.pubSubTemplate = pubSubTemplate;
    this.mapper = mapper;
  }

  @PostMapping("/topics")
  public void createTopic(@RequestBody CreateTopicRequest request) {
    pubSubAdmin.createTopic(request.topicName);
  }

  @DeleteMapping("/topics/{topic}")
  public void deleteTopic(@PathVariable("topic") String topicName) {
    pubSubAdmin.deleteTopic(topicName);
  }

  @PostMapping("/subscriptions")
  public void createSubscription(@RequestBody CreateSubscriptionRequest request) {
    pubSubAdmin.createSubscription(request.subscriptionName, request.topicName);
  }

  @DeleteMapping("/subscriptions/{subscription}")
  public void deleteSubscription(@PathVariable("subscription") String subscriptionName) {
    pubSubAdmin.deleteSubscription(subscriptionName);
  }

  @PostMapping("/topics/{topic}/publish")
  public void publish(
      @PathVariable("topic") String topicName, @RequestBody NotificationPayload payload)
      throws JsonProcessingException {
    String json = mapper.writeValueAsString(payload);
    pubSubTemplate.publish(topicName, json, null);
  }

  @PostMapping("/subscriptions/{subscription}/subscribe")
  public void subscribe(@PathVariable("subscription") String subscription) {
    pubSubTemplate.subscribe(
        subscription,
        (message, consumer) -> {
          LOGGER.info(
              "Message received from $subscription subscription. {}",
              message.getData().toStringUtf8());
          consumer.ack();
        });
  }

  static class CreateTopicRequest {
    public String topicName;

    public CreateTopicRequest() {}
  }

  static class CreateSubscriptionRequest {
    public String topicName;
    public String subscriptionName;

    public CreateSubscriptionRequest() {}
  }
}
