package com.yoloo.server.feed.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeedAggregator {

  private final List<ItemFetcher<?>> items;

  private FeedAggregator(Builder builder) {
    this.items = builder.items;

    for (int i = 0; i < items.size(); i++) {

    }

    items.sort(Comparator.comparingInt(o -> o.order(5, 50)));
  }

  public static Builder newBuilder(int limit) {
    return new Builder(limit);
  }

  public static class Builder {
    private List<ItemFetcher<?>> items;

    private Builder(int limit) {
      this.items = new ArrayList<>(limit);
    }

    public Builder addFetcher(ItemFetcher<?> fetcher) {
      items.add(fetcher);
      return this;
    }

    public FeedAggregator build() {
      return new FeedAggregator(this);
    }
  }
}
