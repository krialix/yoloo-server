package com.yoloo.server.common.counter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface CounterService {

  default Long get(String key) {
    return getAll(Collections.singletonList(key)).get(key);
  }

  Map<String, Long> getAll(Collection<String> keys);

  default Long increase(String key, Payload value) {
    return increase(Collections.singletonMap(key, value)).get(key);
  }

  Map<String, Long> increase(Map<String, Payload> payloads);

  default Long decrease(String key, Payload value) {
    return decrease(Collections.singletonMap(key, value)).get(key);
  }

  Map<String, Long> decrease(Map<String, Payload> payloads);
}
