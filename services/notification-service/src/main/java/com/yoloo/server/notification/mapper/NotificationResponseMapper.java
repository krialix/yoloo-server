package com.yoloo.server.notification.mapper;

import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.vo.NotificationResponse;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationResponseMapper {

  public NotificationResponse apply(Notification from) {
    return NotificationResponse.newBuilder()
        .id(from.getId())
        .actor(
            NotificationResponse.ActorResponse.newBuilder()
                .id(from.getActor().getId())
                .avatarUrl(from.getActor().getAvatarUrl())
                .displayName(from.getActor().getDisplayName())
                .build())
        .type(from.getType().name())
        .receiver(
            NotificationResponse.ReceiverResponse.newBuilder()
                .id(from.getReceiver().getId())
                .build())
        .build();
  }
}
