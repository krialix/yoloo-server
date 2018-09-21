package com.yoloo.server.entity;

import com.googlecode.objectify.Key;
import org.jetbrains.annotations.NotNull;

public interface Keyable<T> extends Comparable<Keyable<T>> {

  @SuppressWarnings("unchecked")
  default Key<T> getKey() {
    return Key.create((T) this);
  }

  @Override
  default int compareTo(@NotNull Keyable<T> o) {
    return getKey().compareTo(o.getKey());
  }
}
