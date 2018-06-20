package com.yoloo.server.notification.pubsub;

import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.entity.Notification;

interface PubSubState {

  class Data implements PubSubState {
    private final String messageId;
    private final Message message;
    private final Notification notification;

    Data(String messageId, Message message, Notification notification) {
      this.messageId = messageId;
      this.message = message;
      this.notification = notification;
    }

    String getMessageId() {
      return messageId;
    }

    Message getMessage() {
      return message;
    }

    Notification getNotification() {
      return notification;
    }

    @Override
    public String toString() {
      return "Data{"
          + "messageId='"
          + messageId
          + '\''
          + ", message="
          + message
          + ", notification="
          + notification
          + '}';
    }
  }

  class Empty implements PubSubState {}

  class Error implements PubSubState {
    private final Throwable throwable;

    Error(Throwable throwable) {
      this.throwable = throwable;
    }

    Throwable getThrowable() {
      return throwable;
    }

    @Override
    public String toString() {
      return "Error{" + "throwable=" + throwable + '}';
    }
  }
}
