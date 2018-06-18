package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.notification.payload.NotificationBody;
import com.yoloo.server.notification.payload.NotificationPayload;
import com.yoloo.server.notification.entity.Notification;

import java.util.Optional;

public class FollowMessageProvider extends MessageProvider {

  @Override
  protected Optional<Pair<Message, Notification>> check(NotificationPayload payload) {
    if (payload.getType().equals("TYPE_FOLLOW")) {
      NotificationBody.Follow body = (NotificationBody.Follow) payload.getBody();

      Message message = Message.builder()
          .setToken(body.getToken())
          .putData("FCM_KEY_TYPE", "TYPE_FOLLOW")
          .putData("FCM_KEY_FOLLOWER_ID", body.getFollowerId())
          .putData("FCM_KEY_FOLLOWER_NAME", body.getFollowerName())
          .putData("FCM_KEY_FOLLOWER_IMAGE", body.getFollowerImage())
          .build();

      Notification notification = new Notification();

      return Optional.of(new Pair<>(message, notification));
    }

    return checkNext(payload);
  }
}
