package com.yoloo.server.common.configuration;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
