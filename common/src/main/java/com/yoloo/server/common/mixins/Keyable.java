package com.yoloo.server.common.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;

public interface Keyable<T> {

  @JsonIgnore
  default Key<T> getKey() {
    return Key.create((T) this);
  }
}
