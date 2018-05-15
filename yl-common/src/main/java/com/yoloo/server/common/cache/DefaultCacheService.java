package com.yoloo.server.common.cache;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

@Primary
@Component
public class DefaultCacheService implements CacheService {

  private final AsyncMemcacheService memcacheService;

  public DefaultCacheService(AsyncMemcacheService memcacheService) {
    this.memcacheService = memcacheService;
  }

  @Override
  public Future<Object> getAsync(Object key) {
    return memcacheService.get(key);
  }

  @Override
  public <T> Future<Map<T, Object>> getAllAsync(Collection<T> keys) {
    return memcacheService.getAll(keys);
  }

  @Override
  public Future<Void> putAllAsync(Map<?, ?> value) {
    return memcacheService.putAll(value);
  }
}
