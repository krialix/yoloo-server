package com.yoloo.server.common.queue.config;

public final class QueueBeanQualifier {
  public static final String NOTIFICATION = "notification-pull-queue";
  public static final String GAMIFICATION = "gamification-pull-queue";
  public static final String SEARCH = "search-pull-queue";
  public static final String FEED = "feed-queue";
  public static final String PULL_QUEUE = "pull-queue";

  private QueueBeanQualifier() {}
}
