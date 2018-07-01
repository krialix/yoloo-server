package com.yoloo.server.feed;

import com.yoloo.server.feed.core.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedAggregator {

  private final List<FeedItem> feedItems = new ArrayList<>();

  private FeedAggregator(Builder builder) {
    ItemFetcher itemFetcher = builder.itemFetcher;
    int limit = builder.limit;
    int totalConsumedCount = 100;

    for (int i = 1; i <= limit; i++) {
      itemFetcher.fetch(totalConsumedCount, limit)
          .ifPresent(feedItems::add);
    }

    /*for(int i = 1; i <= limit; i++) {
      for (ItemFetcher fetcher : fetchers) {
        if (fetcher.matches(totalConsumedCount, limit)) {
          FeedItem item = fetcher.getFromLast(i);
          feedItems.add(item);
          ++totalConsumedCount;
          break;
        }
      }
    }*/
  }

  public static Builder newBuilder(int limit) {
    return new Builder(limit);
  }

  public List<FeedItem> getFeedItems() {
    return feedItems;
  }

  public static class Builder {
    private List<FeedItem> defaultItems;
    private ItemFetcher itemFetcher;
    private int limit;
    private String cursor;

    private Builder(int limit) {
      this.limit = limit;
      this.defaultItems = new ArrayList<>(this.limit);
    }

    public Builder defaultFeedItems(List<FeedItem> defaultItems) {
      this.defaultItems = defaultItems;
      return this;
    }

    public Builder addFetcher(ItemFetcher fetcher) {
      itemFetcher.setNextChain(fetcher);
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
