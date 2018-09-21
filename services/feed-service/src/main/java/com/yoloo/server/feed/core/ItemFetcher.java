package com.yoloo.server.feed.core;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemFetcher<T extends FeedItem> implements Comparable<ItemFetcher<T>> {

  private final List<T> cached;

  protected ItemFetcher() {
    cached = new ArrayList<>();
  }

  public abstract boolean matches(int totalConsumedCount, int requestedCount);

  public abstract int order();

  public T getFromLast(int i) {
    if (cached.isEmpty()) {
      List<T> fetched = fetch();
      cached.addAll(fetched);
    }
    return cached.get(cached.size() - i);
  }

  protected abstract List<T> fetch();

  @Override
  public int compareTo(ItemFetcher<T> o) {
    return order() - o.order();
  }
}
