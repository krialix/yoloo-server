package com.yoloo.server.feed;

import com.yoloo.server.feed.core.FeedItem;

import java.util.Optional;

public abstract class ItemFetcher {

  private ItemFetcher next;

  protected ItemFetcher setNextChain(ItemFetcher fetcher) {
    next = fetcher;
    return this;
  }

  public abstract Optional<FeedItem> fetch(int totalConsumedCount, int requestedCount);

  protected Optional<FeedItem> fetchNext(int totalConsumedCount, int requestedCount) {
    if (next == null) {
      ++totalConsumedCount;
      return Optional.empty();
    }

    return next.fetch(totalConsumedCount, requestedCount);
  }
}
