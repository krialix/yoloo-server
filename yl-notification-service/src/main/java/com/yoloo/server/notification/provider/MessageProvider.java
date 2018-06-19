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
    return next;
  }

  public abstract Pair<Message, Notification> check(NotificationPayload payload);

  protected Pair<Message, Notification> checkNext(NotificationPayload payload) {
    if (next == null) {
      return null;
    }
    LOGGER.info("Processing next message provider: {}", next.getClass().getSimpleName());
    return next.check(payload);
  }
}
