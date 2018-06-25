package com.yoloo.server.common.queue.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.yoloo.server.common.queue.service.NotificationService;
import com.yoloo.server.common.queue.service.NotificationServiceImpl;
import com.yoloo.server.common.queue.service.SearchService;
import com.yoloo.server.common.queue.service.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ServiceAutoConfiguration {

  @Lazy
  @Bean
  public NotificationService notificationService(
      @Qualifier(QueueBeanQualifier.NOTIFICATION) Queue notificationQueue,
      ObjectMapper objectMapper) {
    return new NotificationServiceImpl(notificationQueue, objectMapper);
  }

  @Lazy
  @Bean
  public SearchService searchService(
      @Qualifier(QueueBeanQualifier.SEARCH) Queue searchQueue, ObjectMapper objectMapper) {
    return new SearchServiceImpl(searchQueue, objectMapper);
  }
}
