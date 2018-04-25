package com.yoloo.server.common.counter;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CounterServiceImpl implements CounterService {

  private static final Map<String, Long> CACHE = new ConcurrentHashMap<>();

  @Override
  public Map<String, Long> getAll(Collection<String> keys) {
    return null;
  }

  @Override
  public Map<String, Long> increase(Map<String, Payload> payloads) {
    payloads.forEach((key, value) -> {
      if (!CACHE.containsKey(key)) {
        CACHE.put(key, 1L);
      } else {
        CACHE.compute(key, (key1, value2) -> ++value2);
      }
    });

    payloads.forEach((key, payload) -> CACHE.get(key));
    return null;
  }

  @Override
  public Map<String, Long> decrease(Map<String, Payload> payloads) {
    return null;
  }
}
