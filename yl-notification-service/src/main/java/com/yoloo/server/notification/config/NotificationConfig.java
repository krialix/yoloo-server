package com.yoloo.server.notification.config;

import com.yoloo.server.notification.mapper.NotificationResponseMapper;
import com.yoloo.server.notification.usecase.ListNotificationsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class NotificationConfig {

  @Bean
  @Lazy
  public NotificationResponseMapper notificationResponseMapper() {
    return new NotificationResponseMapper();
  }

  @Bean
  @Lazy
  public ListNotificationsUseCase listNotificationsUseCase(NotificationResponseMapper mapper) {
    return new ListNotificationsUseCase(mapper);
  }
}
