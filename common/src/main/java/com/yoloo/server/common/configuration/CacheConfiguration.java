package com.yoloo.server.common.configuration;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

  @Bean
  public CacheManager cacheManager() {
    return CacheManagerBuilder.newCacheManagerBuilder().build(true);
  }
}
