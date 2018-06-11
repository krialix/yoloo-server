package com.yoloo.server.feed.core;

import java.util.function.Supplier;

public interface ItemFetcher<T> extends Supplier<T> {

  int order(int consumed, int total);
}
