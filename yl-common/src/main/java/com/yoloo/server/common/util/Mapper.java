package com.yoloo.server.common.util;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;

public interface Mapper<F, T> {

  @Nonnull
  default T apply(@Nonnull F from) {
    return apply(from, Collections.emptyMap());
  }

  @Nonnull
  T apply(@Nonnull F from, @Nonnull Map<String, Object> payload);
}
