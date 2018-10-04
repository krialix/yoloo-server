package com.yoloo.server.filter;

import com.googlecode.objectify.Key;

import javax.annotation.Nonnull;
import java.util.Map;

public interface FilterService {

  <T extends Filter> Map<Key<T>, Object> get(@Nonnull Key<T>... keys);

  void save(Filter filter);

  void saveAsync(Filter... filters);
}
