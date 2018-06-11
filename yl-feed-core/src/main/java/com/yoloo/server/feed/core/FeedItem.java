package com.yoloo.server.feed.core;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface FeedItem<I> {

  Supplier<I> getItem(String cursor);

  Predicate<?> test(int consumed, int total, Predicate<?> predicate);
}
