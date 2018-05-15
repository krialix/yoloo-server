package com.yoloo.server.common.config;

import com.google.appengine.api.LifecycleManager;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.yoloo.server.common.event.AppengineShutdownEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppengineConfig {

  @Autowired
  public AppengineConfig(ApplicationEventPublisher eventPublisher) {
    LifecycleManager.getInstance()
        .setShutdownHook(
            () -> {
              LifecycleManager.getInstance().interruptAllRequests();
              eventPublisher.publishEvent(new AppengineShutdownEvent(this));
            });
  }

  @Bean
  public URLFetchService urlFetchService() {
    return URLFetchServiceFactory.getURLFetchService();
  }

  @Bean
  public MemcacheService memcacheService() {
    return MemcacheServiceFactory.getMemcacheService();
  }

  @Bean
  public AsyncMemcacheService asyncMemcacheService() {
    return MemcacheServiceFactory.getAsyncMemcacheService();
  }

  @Primary
  @Bean
  public Queue defaultQueue() {
    return QueueFactory.getDefaultQueue();
  }

  @Bean("subscription-queue")
  public Queue subscriptionQueue() {
    return QueueFactory.getQueue("subscription-queue");
  }

  @Bean("notification-queue")
  public Queue notificationQueue() {
    return QueueFactory.getQueue("notification-queue");
  }

  @Bean("refresh-feed-queue")
  public Queue refreshFeedQueue() {
    return QueueFactory.getQueue("refresh-feed-queue");
  }
}
