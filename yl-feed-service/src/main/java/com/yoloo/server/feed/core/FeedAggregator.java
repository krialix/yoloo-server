package com.yoloo.server.feed.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedAggregator {

  private final List<FeedItem> feedItems = new ArrayList<>();

  private FeedAggregator(Builder builder) {
    List<ItemFetcher<FeedItem>> fetchers = builder.items;
    int limit = builder.limit;
    int totalConsumedCount = 100;

    Collections.sort(fetchers);

    for(int i = 1; i <= limit; i++) {
      for (ItemFetcher<FeedItem> fetcher : fetchers) {
        if (fetcher.matches(totalConsumedCount, limit)) {
          FeedItem item = fetcher.getFromLast(i);
          feedItems.add(item);
          ++totalConsumedCount;
          break;
        }
      }
    }
  }

  public static Builder newBuilder(int limit) {
    return new Builder(limit);
  }

  public List<FeedItem> getFeedItems() {
    return feedItems;
  }

  public static class Builder {
    private List<ItemFetcher<FeedItem>> items;
    private int limit;
    private String cursor;

    private Builder(int limit) {
      this.limit = limit;
      this.items = new ArrayList<>(limit);
    }

    public Builder addFetcher(ItemFetcher<FeedItem> fetcher) {
      items.add(fetcher);
      return this;
    }

    public Builder cursor(String cursor) {
      this.cursor = cursor;
      return this;
    }

    public FeedAggregator build() {
      return new FeedAggregator(this);
    }
  }
}
