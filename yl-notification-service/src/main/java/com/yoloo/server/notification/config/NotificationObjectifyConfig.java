package com.yoloo.server.notification.config;

import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class NotificationObjectifyConfig implements ObjectifyConfigurer {

  @Override
  public Collection<Class<?>> registerEntities() {
    return Collections.singletonList(Notification.class);
  }
}
