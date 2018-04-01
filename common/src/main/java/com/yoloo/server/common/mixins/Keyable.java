package com.yoloo.server.common.mixins;

import com.googlecode.objectify.Key;

public interface Keyable<T> {

  default Key<T> getKey() {
    return Key.create((T) this);
  }
}
