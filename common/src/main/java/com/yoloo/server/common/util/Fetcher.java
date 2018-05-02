package com.yoloo.server.common.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public interface Fetcher<T> {

  default T fetch(Long id) throws IOException {
    return fetchAll(Collections.singleton(id)).iterator().next();
  }

  Collection<T> fetchAll(Collection<Long> ids) throws IOException;
}
