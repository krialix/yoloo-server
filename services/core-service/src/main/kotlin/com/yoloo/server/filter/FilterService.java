package com.yoloo.server.filter;

import com.googlecode.objectify.Key;

import javax.annotation.Nonnull;
import java.util.Map;

public interface FilterService {

  default <T extends Filter> T get(@Nonnull Key<T> key) {
    //noinspection unchecked
    return getAll(key).get(key);
  }

  @SuppressWarnings("unchecked")
  <T extends Filter> Map<Key<T>, T> getAll(@Nonnull Key<T>... keys);

  void saveAsync(Filter... filters);
}
