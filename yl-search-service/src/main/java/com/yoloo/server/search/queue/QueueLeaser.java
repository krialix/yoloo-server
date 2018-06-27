package com.yoloo.server.search.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.common.queue.config.QueueBeanQualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class QueueLeaser {
  private static final Logger LOGGER = LogManager.getLogger();

  private static final int NUMBER_OF_TASK_TO_LEASE = 100;

  private final List<TaskHandle> pendingTasksToDelete = new ArrayList<>();

  private final Queue pullQueue;
  private final ObjectMapper objectMapper;

  public QueueLeaser(
      @Qualifier(QueueBeanQualifier.PULL_QUEUE) Queue pullQueue, ObjectMapper objectMapper) {
    this.pullQueue = pullQueue;
    this.objectMapper = objectMapper;
  }

  private static void logState(EventState state) {
    LOGGER.info("Processing: taskName='{}'", state.getTask().getName());

    if (state instanceof EventState.Error) {
      Throwable error = ((EventState.Error) state).getError();
      LOGGER.error("Processing: taskName='{}' is failed", state.getTask().getName(), error);
    }
  }

  @Scheduled(fixedRate = 5000L, initialDelay = 5000L)
  public void leaseQueue() {
    LOGGER.info("Pulling {} Tasks from the Task Queue", NUMBER_OF_TASK_TO_LEASE);

    pullQueue
        .leaseTasksByTag(3600, TimeUnit.SECONDS, NUMBER_OF_TASK_TO_LEASE, "search")
        .stream()
        .map(this::mapToEventState)
        .peek(QueueLeaser::logState)
        .filter(state -> state instanceof EventState.Data)
        .map(state -> (EventState.Data) state)
        .peek(state -> pendingTasksToDelete.add(state.task))
        .map(state -> state.event)
        .collect(Collectors.groupingBy(event -> event.getMetadata().getType()))
        .forEach(this::consumeEvents);

    int size = pendingTasksToDelete.size();
    if (size == 0) {
      LOGGER.info("Task Queue has no tasks available for lease.");
    } else {
      LOGGER.info("Processed and deleted '{}' tasks from the pull queue", size);
    }

    pullQueue.deleteTaskAsync(pendingTasksToDelete);
  }

  private EventState mapToEventState(TaskHandle task) {
    try {
      return new EventState.Data(task, objectMapper.readValue(task.getPayload(), YolooEvent.class));
    } catch (IOException e) {
      return new EventState.Error(task, e);
    }
  }

  private void consumeEvents(EventType eventType, List<YolooEvent> events) {
    switch (eventType) {
      case NEW_POST:
        break;
      case UPDATE_POST:
        break;
      case DELETE_POST:
        break;
      case NEW_BUDDY_REQUEST:
        break;
      case UPDATE_BUDDY_REQUEST:
        break;
      case DELETE_BUDDY_REQUEST:
        break;
      case NEW_USER:
        break;
      case UPDATE_USER:
        break;
      case DELETE_USER:
        break;
      case FOLLOW_USER:
        break;
      case NEW_COMMENT:
        break;
      case APPROVE_COMMENT:
        break;
    }
  }

  private interface EventState {
    TaskHandle getTask();

    final class Data implements EventState {
      private final TaskHandle task;
      private final YolooEvent event;

      Data(TaskHandle task, YolooEvent event) {
        this.task = task;
        this.event = event;
      }

      @Override
      public TaskHandle getTask() {
        return task;
      }

      YolooEvent getEvent() {
        return event;
      }
    }

    final class Error implements EventState {
      private final TaskHandle task;
      private final Throwable error;

      Error(TaskHandle task, Throwable error) {
        this.task = task;
        this.error = error;
      }

      @Override
      public TaskHandle getTask() {
        return task;
      }

      Throwable getError() {
        return error;
      }
    }
  }
}
