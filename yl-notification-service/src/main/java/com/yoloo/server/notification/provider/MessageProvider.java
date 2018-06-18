package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NotificationPayload;

import java.util.Optional;

public abstract class MessageProvider {

  private MessageProvider next;

  public MessageProvider setNext(MessageProvider next) {
    this.next = next;
    return next;
  }

  public abstract Optional<Pair<Message, Notification>> check(NotificationPayload payload);

  protected Optional<Pair<Message, Notification>> checkNext(NotificationPayload payload) {
    return payload == null ? Optional.empty() : next.check(payload);
  }
}
