package com.yoloo.server.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchSaveDeletePullQueueLeaser {

  private final DefaultBatchSaveDeleteQueueLeaser defaultBatchSaveDeleteQueueLeaser;

  @Autowired
  public BatchSaveDeletePullQueueLeaser(
      DefaultBatchSaveDeleteQueueLeaser defaultBatchSaveDeleteQueueLeaser) {
    this.defaultBatchSaveDeleteQueueLeaser = defaultBatchSaveDeleteQueueLeaser;
  }

  @Scheduled(cron = "0 */5 * * *")
  public void leaseDefaultTag() {
    defaultBatchSaveDeleteQueueLeaser.run();
  }

  @Scheduled(cron = "0 */10 * * *")
  public void leaseCounterTag() {
    defaultBatchSaveDeleteQueueLeaser.run();
  }
}
