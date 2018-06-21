package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NewCommentBody;
import com.yoloo.server.notification.payload.NotificationPayload;

public class NewCommentMessageProvider extends MessageProvider {

  @Override
  public Pair<Message, Notification> check(NotificationPayload payload) {
    if (payload.getType().equals("TYPE_NEW_COMMENT")) {
      NewCommentBody body = (NewCommentBody) payload.getBody();

      Message message =
          Message.builder()
              .setToken(body.getToken())
              .putData("FCM_KEY_TYPE", "TYPE_NEW_COMMENT")
              .putData("FCM_KEY_POST_ID", body.getPostId())
              .putData("FCM_KEY_COMMENT_CONTENT", body.getTrimmedCommentContent())
              .build();

      return new Pair<>(message, null);
    }

    return checkNext(payload);
  }
}
