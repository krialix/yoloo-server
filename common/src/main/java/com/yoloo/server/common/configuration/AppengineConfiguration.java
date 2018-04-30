package com.yoloo.server.common.configuration;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppengineConfiguration {

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
  public Queue getDefaultQueue() {
    return QueueFactory.getDefaultQueue();
  }

  @Bean("counter-queue")
  public Queue getCounterQueue() {
    return QueueFactory.getQueue("counter-queue");
  }

  @Bean("notification-queue")
  public Queue getNotificationQueue() {
    return QueueFactory.getQueue("notification-queue");
  }
}
