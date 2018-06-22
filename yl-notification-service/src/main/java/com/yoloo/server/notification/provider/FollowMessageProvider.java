package com.yoloo.server.notification.provider;

import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.yoloo.server.common.id.generator.LongIdGenerator;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.payload.FollowBody;
import com.yoloo.server.notification.payload.NotificationPayload;

public class FollowMessageProvider extends MessageProvider {

  private final LongIdGenerator idGenerator;

  public FollowMessageProvider(LongIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @Override
  public Pair<Message, Notification> check(NotificationPayload payload) {
    if (payload.getType().equals("TYPE_FOLLOW")) {
      FollowBody body = (FollowBody) payload.getBody();

      Message message =
          Message.builder()
              .setToken(body.getToken())
              .putData("FCM_KEY_TYPE", "TYPE_FOLLOW")
              .putData("FCM_KEY_FOLLOWER_ID", body.getFollowerId())
              .putData("FCM_KEY_FOLLOWER_NAME", body.getFollowerName())
              .putData("FCM_KEY_FOLLOWER_IMAGE", body.getFollowerImage())
              .build();

      Notification notification =
          Notification.newBuilder()
              .id(idGenerator.generateId())
              .type(Notification.EntityType.FOLLOW)
              .actor(
                  Notification.Actor.newBuilder()
                      .id(Long.parseLong(body.getFollowerId()))
                      .displayName(body.getFollowerName())
                      .avatarUrl(body.getFollowerImage())
                      .build())
              .receiver(
                  Notification.Receiver.newBuilder()
                      .id(Long.parseLong(body.getFollowingId()))
                      .build())
              .build();

      return new Pair<>(message, notification);
    }

    return checkNext(payload);
  }
}
