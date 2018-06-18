package com.yoloo.server.notification.payload;

public class NotificationPayload {
  private String type;
  private NotificationBody body;

  public NotificationPayload() {
  }

  public String getType() {
    return type;
  }

  public NotificationBody getBody() {
    return body;
  }
}
