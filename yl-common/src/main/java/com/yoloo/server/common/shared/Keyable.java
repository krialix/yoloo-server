package com.yoloo.server.common.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;

public interface Keyable<T> {

  @SuppressWarnings("unchecked")
  @JsonIgnore
  default Key<T> getKey() {
    return Key.create((T) this);
  }
}
