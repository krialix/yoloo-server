package com.yoloo.server.notification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
class SchedulerConfig {

  @Bean
  TaskScheduler taskScheduler() {
    return new ConcurrentTaskScheduler();
  }
}
