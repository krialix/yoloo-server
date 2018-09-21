package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NotificationPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MessageProvider {

  protected static final Logger LOGGER = LogManager.getLogger();

  private MessageProvider next;

  public MessageProvider setNext(MessageProvider next) {
    this.next = next;
    return this;
  }

  protected abstract boolean matches(String type);

  public Pair<Message, Notification> process(NotificationPayload payload) {
    if (matches(payload.getType())) {
      return processInternal(payload);
    }

    return processNext(payload);
  }

  protected abstract Pair<Message, Notification> processInternal(NotificationPayload payload);

  protected Pair<Message, Notification> processNext(NotificationPayload payload) {
    if (next == null) {
      return null;
    }
    LOGGER.info("Processing next message provider: {}", next.getClass().getSimpleName());
    return next.process(payload);
  }
}
