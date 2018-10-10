package com.yoloo.server.counter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.yoloo.server.queue.QueueNames;
import com.yoloo.server.queue.QueuePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static java.util.stream.Collectors.*;

@Component
public class CounterQueueLeaser {

  private final Queue queue;
  private final ObjectMapper objectMapper;

  @Autowired
  public CounterQueueLeaser(
      @Qualifier(QueueNames.BATCH_SAVE_PULL_QUEUE) Queue queue, ObjectMapper objectMapper) {
    this.queue = queue;
    this.objectMapper = objectMapper;
  }

  @Scheduled(cron = "0 */10 * * *")
  public void run() {
    List<TaskHandle> tasks =
        queue.leaseTasksByTag(3600, TimeUnit.SECONDS, QueueConstants.maxLeaseCount(), "counter");

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

    queue.deleteTaskAsync(tasks);
  }

  @Nullable
  private QueuePayload readPayload(TaskHandle task) {
    return sneak(() -> objectMapper.readValue(task.getPayload(), QueuePayload.class));
  }
}
