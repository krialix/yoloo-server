package com.yoloo.server.common.config;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.yoloo.server.common.cache.CacheService;
import com.yoloo.server.common.cache.DefaultCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CacheConfig {

  @Lazy
  @Bean
  public CacheService defaultCacheService(AsyncMemcacheService memcacheService) {
    return new DefaultCacheService(memcacheService);
  }
}
