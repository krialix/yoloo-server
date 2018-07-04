package com.yoloo.server.common.queue.config;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class QueueAutoConfiguration {

  @Lazy
  @Primary
  @Bean
  public Queue defaultQueue() {
    return QueueFactory.getDefaultQueue();
  }

  @Lazy
  @Bean(QueueBeanQualifier.NOTIFICATION)
  public Queue notificationQueue() {
    return QueueFactory.getQueue(QueueBeanQualifier.NOTIFICATION);
  }

  @Lazy
  @Bean(QueueBeanQualifier.GAMIFICATION)
  public Queue gamificationQueue() {
    return QueueFactory.getQueue(QueueBeanQualifier.GAMIFICATION);
  }

  @Lazy
  @Bean(QueueBeanQualifier.SEARCH)
  public Queue searchQueue() {
    return QueueFactory.getQueue(QueueBeanQualifier.SEARCH);
  }

  @Lazy
  @Bean(QueueBeanQualifier.FEED)
  public Queue feedQueue() {
    return QueueFactory.getQueue(QueueBeanQualifier.FEED);
  }

  @Lazy
  @Bean(QueueBeanQualifier.PULL_QUEUE)
  public Queue pullQueue() {
    return QueueFactory.getQueue(QueueBeanQualifier.PULL_QUEUE);
  }
}
