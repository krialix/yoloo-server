package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.NotificationBody;
import com.yoloo.server.notification.payload.NotificationPayload;

public class NewPostMessageProvider extends MessageProvider {

  @Override
  public Pair<Message, Notification> check(NotificationPayload payload) {
    if (payload.getType().equals("TYPE_NEW_POST")) {
      NotificationBody.NewPost body = (NotificationBody.NewPost) payload.getBody();
      LOGGER.info("NewPostMessageProvider(): {}", body);

      Message message =
          Message.builder()
              .setTopic(body.getTopic())
              .putData("FCM_KEY_TYPE", "TYPE_NEW_POST")
              .putData("FCM_KEY_POST_ID", body.getPostId())
              .putData("FCM_KEY_POST_TITLE", body.getPostTitle())
              .putData("FCM_KEY_POST_BOUNTY", body.getBounty())
              .putData("FCM_KEY_GROUP_ID", body.getGroupId())
              .build();

      return new Pair<>(message, null);
    }

    return checkNext(payload);
  }
}
