package com.yoloo.server.notification.config;

import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.objectify.ObjectifyConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class NotificationObjectifyConfig implements ObjectifyConfigurer {

  @Override
  public Collection<Class<?>> registerEntities() {
    return Collections.singletonList(Notification.class);
  }
}
