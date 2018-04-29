package com.yoloo.server.common.shared;

import com.googlecode.objectify.Key;

import javax.validation.constraints.NotNull;

public interface NamespaceKeyProvider<T> {

  String createNamespaceId(@NotNull String identifierId);

  Key<T> createNamespaceKey(@NotNull String identifierId);
}
