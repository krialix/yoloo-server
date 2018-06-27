package com.yoloo.server.common.queue.config;

public interface QueueBeanQualifier {
  String NOTIFICATION = "notification-pull-queue";
  String GAMIFICATION = "gamification-pull-queue";
  String SEARCH = "search-pull-queue";
  String FEED = "feed-queue";
  String PULL_QUEUE = "pull-queue";
}
