package com.yoloo.server.search.pubsub;

interface PubSubState {

  class Data implements PubSubState {
    private final String messageId;

    Data(String messageId) {
      this.messageId = messageId;
    }

    String getMessageId() {
      return messageId;
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
