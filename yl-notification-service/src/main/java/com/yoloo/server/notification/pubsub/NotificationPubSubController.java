package com.yoloo.server.notification.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.google.protobuf.ByteString;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NotificationPayload;
import com.yoloo.server.notification.provider.FollowMessageProvider;
import com.yoloo.server.notification.provider.MessageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Component
public class NotificationPubSubController {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final long PUBSUB_CONSUME_INTERVAL = 15000;

  private final PubSubTemplate pubSubTemplate;
  private final ObjectMapper mapper;
  private final MessageProvider messageProvider;

  @Autowired
  public NotificationPubSubController(PubSubTemplate pubSubTemplate, ObjectMapper mapper) {
    this.pubSubTemplate = pubSubTemplate;
    this.mapper = mapper;
    this.messageProvider = new FollowMessageProvider();
  }

  @Scheduled(fixedRate = PUBSUB_CONSUME_INTERVAL)
  @PostConstruct
  public void pullPubSubEvents() {
    LOGGER.info("pullPubSubEvents() is running!");

    pubSubTemplate
        .pull("notification-service", 100, true, null)
        .forEach(
            message -> {
              LOGGER.info("PUBSUB: {}", message);
              ByteString data = message.getData();
              try {
                NotificationPayload payload =
                    mapper.readValue(data.toByteArray(), NotificationPayload.class);
                Optional<Pair<Message, Notification>> pairOpt = messageProvider.check(payload);
              } catch (IOException e) {
                LOGGER.error("Couldn't parse message data", e);
              }
            });
  }
}
