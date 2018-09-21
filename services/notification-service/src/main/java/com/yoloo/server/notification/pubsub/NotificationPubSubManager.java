package com.yoloo.server.notification.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.pubsub.v1.PubsubMessage;
import com.googlecode.objectify.ObjectifyService;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NotificationPayload;
import com.yoloo.server.notification.provider.*;
import com.yoloo.server.notification.util.AppengineEnv;
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory;
import net.cinnom.nanocuckoo.NanoCuckooFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public class NotificationPubSubManager {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final long DUPLICATE_FILTER_CAPACITY = 100_000L;

  private final PubSubTemplate pubSubTemplate;
  private final MessageProvider messageProvider;
  private final FirebaseMessaging firebaseMessaging;
  private final List<Notification> notifications;
  private final NanoCuckooFilter duplicateFilter;

  @Autowired
  public NotificationPubSubManager(
      PubSubTemplate pubSubTemplate,
      FirebaseMessaging firebaseMessaging,
      IdFactory.LongIdGenerator idGenerator) {
    this.pubSubTemplate = pubSubTemplate;
    this.firebaseMessaging = firebaseMessaging;
    this.messageProvider = buildProviders(idGenerator);
    this.notifications = new ArrayList<>();
    this.duplicateFilter = new NanoCuckooFilter.Builder(DUPLICATE_FILTER_CAPACITY).build();
  }

  private static MessageProvider buildProviders(IdFactory.LongIdGenerator idGenerator) {
    return new NewPostMessageProvider()
        .setNext(
            new NewCommentMessageProvider()
                .setNext(
                    new FollowMessageProvider(idGenerator)
                        .setNext(new CommentApproveMessageProvider(idGenerator))));
  }

  @Scheduled(
      fixedRateString = "${yoloo.notification.scheduler.fixed-rate}",
      initialDelayString = "${yoloo.notification.scheduler.initial-delay}")
  public void pullPubSubEvents() {
    LOGGER.info("pullPubSubEvents() is running!");

    pubSubTemplate
        .pullAndAck("notification-service", 500, true)
        .stream()
        .filter(message -> !isDuplicate(message.getMessageId()))
        .map(this::mapToPayload)
        .map(messageProvider::process)
        .forEach(
            pair -> {
              if (pair != null) {
                if (pair.getSecond() != null) {
                  notifications.add(pair.getSecond());
                }

                sendMessageAsync(pair.getFirst());
              }
            });

    saveNotifications(notifications);
  }

  private void saveNotifications(List<Notification> notifications) {
    if (!notifications.isEmpty()) {
      ObjectifyService.run(
          () -> {
            ofy().save().entities(notifications);
            return null;
          });
    }
  }

  private boolean isDuplicate(String messageId) {
    return duplicateFilter.contains(messageId);
  }

  private void sendMessageAsync(Message message) {
    boolean dryRun = !AppengineEnv.isProd();
    ApiFuture<String> future = firebaseMessaging.sendAsync(message, dryRun);
    ApiFutures.addCallback(
        future,
        new ApiFutureCallback<String>() {
          @Override
          public void onFailure(Throwable t) {
            LOGGER.error("Fcm failed.", t);
          }

          @Override
          public void onSuccess(String messageId) {
            LOGGER.info("Fcm sent successfully with message id: {}", messageId);
          }
        });
  }

  private NotificationPayload mapToPayload(PubsubMessage message) {
    return pubSubTemplate
        .getMessageConverter()
        .fromPubSubMessage(message, NotificationPayload.class);
  }
}
