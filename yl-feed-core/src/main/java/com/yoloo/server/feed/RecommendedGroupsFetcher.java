package com.yoloo.server.feed;

import com.yoloo.server.feed.core.FeedItem;

import java.util.Optional;

public class RecommendedGroupsFetcher extends ItemFetcher {

  @Override
  public Optional<FeedItem> fetch(int totalConsumedCount, int requestedCount) {
    if (totalConsumedCount == 0) {
      return Optional.of(new RecommendedGroups());
    }

    return fetchNext(totalConsumedCount, requestedCount);
  }
}
