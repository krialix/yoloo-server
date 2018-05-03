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

  @Bean("subscription-queue")
  public Queue getSubscriptionQueue() {
    return QueueFactory.getQueue("subscription-queue");
  }

  @Bean("relationship-queue")
  public Queue getRelationshipQueue() {
    return QueueFactory.getQueue("relationship-queue");
  }

  @Bean("notification-queue")
  public Queue getNotificationQueue() {
    return QueueFactory.getQueue("notification-queue");
  }

  @Bean("refresh-feed-queue")
  public Queue ferRefreshFeedQueue() {
    return QueueFactory.getQueue("refresh-feed-queue");
  }
}
