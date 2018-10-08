package com.yoloo.server.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static java.util.stream.Collectors.*;

@Component
public class DefaultBatchSaveDeleteQueueLeaser implements Runnable {

  private final Queue batchSaveDeletePullQueue;
  private final ObjectMapper objectMapper;

  @Autowired
  public DefaultBatchSaveDeleteQueueLeaser(
      @Qualifier(QueueNames.BATCH_SAVE_PULL_QUEUE) Queue batchSaveDeletePullQueue,
      ObjectMapper objectMapper) {
    this.batchSaveDeletePullQueue = batchSaveDeletePullQueue;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run() {
    List<TaskHandle> tasks =
        batchSaveDeletePullQueue.leaseTasksByTag(
            3600, TimeUnit.SECONDS, QueueConstants.maxLeaseCount(), "default");

    tasks
        .stream()
        .map(this::readPayload)
        .filter(Objects::nonNull)
        .collect(groupingBy(QueuePayload::getType, mapping(QueuePayload::getPayload, toList())))
        .forEach(
            (type, objects) -> {
              switch (type) {
                case SAVE:
                  ObjectifyService.run(() -> ofy().save().entities(objects));
                  break;
                case DELETE:
                  List<Key<Object>> deleteKeys =
                      objects.stream().map(Key::create).collect(toList());
                  ObjectifyService.run(() -> ofy().delete().keys(deleteKeys));
                  break;
              }
            });

    batchSaveDeletePullQueue.deleteTaskAsync(tasks);
  }

  @Nullable
  private QueuePayload readPayload(TaskHandle task) {
    return sneak(() -> objectMapper.readValue(task.getPayload(), QueuePayload.class));
  }
}
