package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.payload.NotificationPayload;
import com.yoloo.server.notification.entity.Notification;

import java.util.Optional;

public abstract class MessageProvider {

  private MessageProvider next;

  public MessageProvider setNext(MessageProvider next) {
    this.next = next;
    return next;
  }

  protected abstract Optional<Pair<Message, Notification>> check(NotificationPayload payload);

  protected Optional<Pair<Message, Notification>> checkNext(NotificationPayload payload) {
    if (payload == null) {
      return Optional.empty();
    }

    return next.check(payload);
  }
}
