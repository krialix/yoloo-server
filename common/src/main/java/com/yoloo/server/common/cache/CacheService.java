package com.yoloo.server.common.cache;

import com.google.appengine.api.memcache.MemcacheServiceException;

import javax.annotation.Nullable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface CacheService {

  static <T> T quietGet(Future<T> future) {
    try {
      return future.get();
    } catch (InterruptedException var3) {
      Thread.currentThread().interrupt();
      throw new MemcacheServiceException("Unexpected failure", var3);
    } catch (ExecutionException var4) {
      Throwable cause = var4.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      } else if (cause instanceof Error) {
        throw (Error) cause;
      } else {
        throw new UndeclaredThrowableException(cause);
      }
    }
  }

  // Sync
  @Nullable
  default Object get(Object key) {
    return quietGet(getAsync(key));
  }

  default <T> Map<T, Object> getAll(Collection<T> keys) {
    return quietGet(getAllAsync(keys));
  }

  // Async
  Future<Object> getAsync(Object key);

  <T> Future<Map<T, Object>> getAllAsync(Collection<T> keys);
}
