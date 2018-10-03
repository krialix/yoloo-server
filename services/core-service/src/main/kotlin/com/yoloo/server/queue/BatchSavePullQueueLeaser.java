package com.yoloo.server.queue;

import com.google.appengine.api.taskqueue.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchSavePullQueueLeaser {

  private final Queue batchSavePullQueue;

  @Autowired
  public BatchSavePullQueueLeaser(
      @Qualifier(QueueNames.BATCH_SAVE_PULL_QUEUE) Queue batchSavePullQueue) {
    this.batchSavePullQueue = batchSavePullQueue;
  }

  @Scheduled(fixedRate = 100L)
  public void leaseTasks() {
    // batchSavePullQueue.leaseTasks()
  }
}
