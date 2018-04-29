package com.yoloo.server.common.cache;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class DefaultCacheService implements CacheService {

  private final LoadingCache<Object, ?> cache;
  private final AsyncMemcacheService memcacheService;

  public DefaultCacheService(LoadingCache<Object, ?> cache, AsyncMemcacheService memcacheService) {
    this.cache = cache;
    this.memcacheService = memcacheService;
  }

  @Override
  public Future<Object> getAsync(Object key) {
    Object o = cache.get(key);
    return o != null ? CompletableFuture.completedFuture(o) : memcacheService.get(key);
  }

  @Override
  public <T> Future<Map<T, Object>> getAllAsync(Collection<T> keys) {
    return null;
  }
}
