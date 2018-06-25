package com.yoloo.server.common.queue.service;

import com.yoloo.server.common.queue.api.YolooEvent;

public interface QueueService {
  void addQueueAsync(YolooEvent event);

  void addQueueSync(YolooEvent event);
}
