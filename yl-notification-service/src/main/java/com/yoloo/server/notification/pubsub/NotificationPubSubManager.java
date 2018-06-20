package com.yoloo.server.notification.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.pubsub.v1.PubsubMessage;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.VoidWork;
import com.yoloo.server.common.id.config.IdGenQualifier;
import com.yoloo.server.common.id.generator.LongIdGenerator;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NotificationPayload;
import com.yoloo.server.notification.provider.*;
import com.yoloo.server.notification.util.AppengineEnv;
import net.cinnom.nanocuckoo.NanoCuckooFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yoloo.server.objectify.ObjectifyProxy.ofy;

@Component
public class NotificationPubSubManager {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final long PUBSUB_CONSUME_INTERVAL = 5000;

  private final PubSubTemplate pubSubTemplate;
  private final ObjectMapper mapper;
  private final MessageProvider messageProvider;
  private final FirebaseMessaging firebaseMessaging;
  private final List<Notification> notifications;
  private final NanoCuckooFilter duplicateFilter;

  @Autowired
  public NotificationPubSubManager(
      PubSubTemplate pubSubTemplate,
      ObjectMapper mapper,
      FirebaseMessaging firebaseMessaging,
      @Qualifier(IdGenQualifier.CACHED) LongIdGenerator idGenerator) {
    this.pubSubTemplate = pubSubTemplate;
    this.mapper = mapper;
    this.firebaseMessaging = firebaseMessaging;
    this.messageProvider = buildProviders(idGenerator);
    this.notifications = new ArrayList<>();
    this.duplicateFilter = buildDuplicateFilter();
  }

  private static NanoCuckooFilter buildDuplicateFilter() {
    return new NanoCuckooFilter.Builder(32).build();
  }

  private static MessageProvider buildProviders(LongIdGenerator idGenerator) {
    return new NewPostMessageProvider()
        .setNext(
            new NewCommentMessageProvider()
                .setNext(
                    new FollowMessageProvider(idGenerator)
                        .setNext(new CommentApproveMessageProvider(idGenerator))));
  }

  @Scheduled(fixedRate = PUBSUB_CONSUME_INTERVAL)
  public void pullPubSubEvents() {
    LOGGER.info("pullPubSubEvents() is running!");

    pubSubTemplate
        .pull("notification-service", 100, true, null)
        .stream()
        .map(this::mapToPubSubState)
        .peek(state -> LOGGER.info("Processing state {}", state))
        .forEach(
            state -> {
              if (state instanceof PubSubState.Data) {
                PubSubState.Data data = (PubSubState.Data) state;
                if (isDuplicate(data.getMessageId())) {
                  LOGGER.info("Duplicate message found: {}", data.getMessageId());
                  return;
                }

                if (data.getNotification() != null) {
                  notifications.add(data.getNotification());
                }

                sendMessageAsync(data.getMessage());
              } else if (state instanceof PubSubState.Error) {
                PubSubState.Error error = (PubSubState.Error) state;
                LOGGER.error("An error occurred", error.getThrowable());
              }
            });

    saveNotifications(notifications);
  }

  private void saveNotifications(List<Notification> notifications) {
    if (!notifications.isEmpty()) {
      ObjectifyService.run(
          new VoidWork() {
            @Override
            public void vrun() {
              Result<Map<Key<Notification>, Notification>> saveResult =
                  ofy().save().entities(notifications);
              if (AppengineEnv.isTest()) {
                saveResult.now();
              }

              notifications.clear();
            }
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

  private PubSubState mapToPubSubState(PubsubMessage message) {
    try {
      NotificationPayload payload =
          mapper.readValue(message.getData().toByteArray(), NotificationPayload.class);
      Pair<Message, Notification> pair = messageProvider.check(payload);
      if (pair == null) {
        return new PubSubState.Empty();
      }
      return new PubSubState.Data(message.getMessageId(), pair.getFirst(), pair.getSecond());
    } catch (IOException e) {
      return new PubSubState.Error(e);
    }
  }
}
