package com.yoloo.server.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class NotificationPubSubController {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final long PUBSUB_CONSUME_INTERVAL = 15000;

  private final PubSubTemplate pubSubTemplate;
  private final ObjectMapper mapper;

  @Autowired
  NotificationPubSubController(PubSubTemplate pubSubTemplate, ObjectMapper mapper) {
    this.pubSubTemplate = pubSubTemplate;
    this.mapper = mapper;
  }

  @Scheduled(fixedRate = PUBSUB_CONSUME_INTERVAL)
  @PostConstruct
  void pullPubSubEvents() {
    LOGGER.info("pullPubSubEvents() is running!");

    Message.builder().build()

    pubSubTemplate
        .pull("notification-service", 100, true, null)
        .forEach(message -> LOGGER.info("PUBSUB: {}", message));
  }
}
