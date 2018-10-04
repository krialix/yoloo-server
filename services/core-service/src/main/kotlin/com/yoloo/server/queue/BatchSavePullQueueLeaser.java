package com.yoloo.server.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static java.util.stream.Collectors.*;

@Component
public class BatchSavePullQueueLeaser {

  private static final Logger log = LogManager.getLogger();

  private final Queue batchSavePullQueue;
  private final ObjectMapper objectMapper;

  @Autowired
  public BatchSavePullQueueLeaser(
      @Qualifier(QueueNames.BATCH_SAVE_PULL_QUEUE) Queue batchSavePullQueue,
      ObjectMapper objectMapper) {
    this.batchSavePullQueue = batchSavePullQueue;
    this.objectMapper = objectMapper;
  }

  @Scheduled(cron = "0 */5 * * *")
  public void leaseTasks() {
    List<TaskHandle> tasks = batchSavePullQueue.leaseTasks(3600, TimeUnit.SECONDS, 1000);

    processTasks(tasks);
  }

  private void processTasks(List<TaskHandle> tasks) {
    Map<String, List<QueuePayload.Payload>> payloadMap =
        tasks
            .stream()
            .map(this::readPayload)
            .filter(Objects::nonNull)
            .collect(
                groupingBy(QueuePayload::getType, mapping(QueuePayload::getPayload, toList())));

    int savedEntityCount = 0;
    int deletedEntityCount = 0;

    if (payloadMap != null && !payloadMap.isEmpty()) {
      savedEntityCount = saveEntities(payloadMap);
      deletedEntityCount = deleteEntities(payloadMap);
    }

    batchSavePullQueue.deleteTaskAsync(tasks);

    log.info(
        "Saved {} and deleted {} tasks from the task queue.", savedEntityCount, deletedEntityCount);
  }

  private int saveEntities(Map<String, List<QueuePayload.Payload>> payloadMap) {
    List<QueuePayload.Payload> savePayload = payloadMap.get(QueuePayload.Payload.TYPE_SAVE);

    List<Object> saveList =
        savePayload
            .stream()
            .map(payload -> ((QueuePayload.Save) payload))
            .map(QueuePayload.Save::getData)
            .collect(toList());

    ObjectifyService.run(() -> ofy().save().entities(saveList));

    return saveList.size();
  }

  private int deleteEntities(Map<String, List<QueuePayload.Payload>> payloadMap) {
    List<QueuePayload.Payload> deletePayload = payloadMap.get(QueuePayload.Payload.TYPE_DELETE);

    List<Key<?>> deleteList =
        deletePayload
            .stream()
            .map(payload -> ((QueuePayload.Delete) payload))
            .map(QueuePayload.Delete::getUrlSafeString)
            .map(Key::create)
            .collect(toList());

    ObjectifyService.run(() -> ofy().delete().keys(deleteList));

    return deleteList.size();
  }

  @Nullable
  private QueuePayload readPayload(TaskHandle task) {
    try {
      return objectMapper.readValue(task.getPayload(), QueuePayload.class);
    } catch (IOException e) {
      log.error(
          "An error occurred while converting task payload to queue payload for task name {}",
          task.getName());
      return null;
    }
  }
}
