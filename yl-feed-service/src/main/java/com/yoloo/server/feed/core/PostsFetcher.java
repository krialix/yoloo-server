package com.yoloo.server.feed.core;

import java.util.Collections;
import java.util.List;

public class PostsFetcher extends ItemFetcher<Post> {

  @Override
  public boolean matches(int totalConsumedCount, int requestedCount) {
    return true;
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  protected List<Post> fetch() {
    return Collections.emptyList();
  }
}
