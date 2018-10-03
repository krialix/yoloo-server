package com.yoloo.server.queue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class QueueConfig {

  @Lazy
  @Bean(QueueNames.BATCH_SAVE_PULL_QUEUE)
  public Queue batchSavePullQueue() {
    return QueueFactory.getQueue(QueueNames.BATCH_SAVE_PULL_QUEUE);
  }
}
