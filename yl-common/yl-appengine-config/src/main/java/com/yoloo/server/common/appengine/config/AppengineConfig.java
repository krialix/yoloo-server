package com.yoloo.server.common.appengine.config;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppengineConfig {

  @Lazy
  @Bean
  public ImagesService imagesService() {
    return ImagesServiceFactory.getImagesService();
  }

  @Lazy
  @Bean
  public URLFetchService urlFetchService() {
    return URLFetchServiceFactory.getURLFetchService();
  }

  @Lazy
  @Bean
  public MemcacheService memcacheService() {
    return MemcacheServiceFactory.getMemcacheService();
  }

  @Lazy
  @Bean
  public AsyncMemcacheService asyncMemcacheService() {
    return MemcacheServiceFactory.getAsyncMemcacheService();
  }

  @Lazy
  @Primary
  @Bean
  public Queue defaultQueue() {
    return QueueFactory.getDefaultQueue();
  }
}
