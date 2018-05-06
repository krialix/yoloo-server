package com.yoloo.server.common.configuration;

import com.google.appengine.api.ThreadManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SpringEventConfiguration {

  @Bean(name = "applicationEventMulticaster")
  public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
    SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

    ExecutorService executor = Executors.newCachedThreadPool(ThreadManager.backgroundThreadFactory());

    eventMulticaster.setTaskExecutor(executor);
    return eventMulticaster;
  }
}
