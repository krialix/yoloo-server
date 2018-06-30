package com.yoloo.server.common.queue.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.yoloo.server.common.queue.service.NotificationQueueService;
import com.yoloo.server.common.queue.service.NotificationQueueServiceImpl;
import com.yoloo.server.common.queue.service.SearchQueueService;
import com.yoloo.server.common.queue.service.SearchQueueServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ServiceAutoConfiguration {

  @Lazy
  @Bean
  public NotificationQueueService notificationService(
      @Qualifier(QueueBeanQualifier.PULL_QUEUE) Queue notificationQueue,
      ObjectMapper objectMapper) {
    return new NotificationQueueServiceImpl(notificationQueue, objectMapper);
  }

  @Lazy
  @Bean
  public SearchQueueService searchService(
      @Qualifier(QueueBeanQualifier.PULL_QUEUE) Queue searchQueue, ObjectMapper objectMapper) {
    return new SearchQueueServiceImpl(searchQueue, objectMapper);
  }
}
