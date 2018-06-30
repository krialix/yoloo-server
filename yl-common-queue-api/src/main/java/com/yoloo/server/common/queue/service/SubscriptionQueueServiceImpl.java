package com.yoloo.server.common.queue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.yoloo.server.common.queue.vo.YolooEvent;

import java.io.IOException;

public class SubscriptionQueueServiceImpl implements SubscriptionQueueService {
  private final Queue queue;
  private final ObjectMapper mapper;
  private final AsyncMemcacheService asyncMemcacheService;

  public SubscriptionQueueServiceImpl(
      Queue queue, ObjectMapper mapper, AsyncMemcacheService asyncMemcacheService) {
    this.queue = queue;
    this.mapper = mapper;
    this.asyncMemcacheService = asyncMemcacheService;
  }

  @Override
  public void addQueueAsync(YolooEvent event) {
    try {
      String json = mapper.writeValueAsString(event);
      queue.addAsync(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(json));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addQueueSync(YolooEvent event) {
    try {
      String json = mapper.writeValueAsString(event);
      queue.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(json));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
