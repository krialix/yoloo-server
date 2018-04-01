package com.yoloo.server.common.mixins;

import com.googlecode.objectify.Key;

import javax.validation.constraints.NotNull;

public interface NamespacedKeyProvider {

  String createNamespacedId(@NotNull String identifierId);

  default <T> Key<T> createNamespacedKey(@NotNull String identifierId) {
    return Key.create(createNamespacedId(identifierId));
  }
}
