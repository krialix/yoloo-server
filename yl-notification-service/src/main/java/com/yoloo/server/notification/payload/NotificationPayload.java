package com.yoloo.server.notification.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class NotificationPayload {
  private String type;

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
      property = "type")
  @JsonSubTypes({
    @JsonSubTypes.Type(value = NotificationBody.NewPost.class, name = "TYPE_NEW_POST"),
    @JsonSubTypes.Type(value = NotificationBody.NewComment.class, name = "TYPE_NEW_COMMENT"),
    @JsonSubTypes.Type(value = NotificationBody.Approve.class, name = "TYPE_APPROVE"),
    @JsonSubTypes.Type(value = NotificationBody.Follow.class, name = "TYPE_FOLLOW"),
  })
  private NotificationBody body;

  public NotificationPayload() {}

  public String getType() {
    return type;
  }

  public NotificationBody getBody() {
    return body;
  }
}
