package com.yoloo.server.common.queue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.common.queue.config.QueueEndpoint;

import java.io.IOException;

public class SearchServiceImpl implements SearchService {
  private final Queue queue;
  private final ObjectMapper mapper;

  public SearchServiceImpl(Queue queue, ObjectMapper mapper) {
    this.queue = queue;
    this.mapper = mapper;
  }

  @Override
  public void addQueueAsync(YolooEvent event) {
    try {
      String json = mapper.writeValueAsString(event);
      queue.addAsync(
          TaskOptions.Builder.withUrl(QueueEndpoint.QUEUE_SEARCH_ENDPOINT).payload(json));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addQueueSync(YolooEvent event) {
    try {
      String json = mapper.writeValueAsString(event);
      queue.add(
          TaskOptions.Builder.withUrl(QueueEndpoint.QUEUE_SEARCH_ENDPOINT).param("data", json));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
