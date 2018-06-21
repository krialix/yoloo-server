package com.yoloo.server.notification.config;

import com.google.appengine.api.ThreadManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setTaskScheduler(taskScheduler());
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadFactory threadFactory = ThreadManager.backgroundThreadFactory();
    ScheduledExecutorService executorService =
        Executors.newSingleThreadScheduledExecutor(threadFactory);
    return new ConcurrentTaskScheduler(executorService);
  }
}
