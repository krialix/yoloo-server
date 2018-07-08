package com.yoloo.server.search.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.yoloo.server.common.queue.config.QueueBeanQualifier;
import com.yoloo.server.common.queue.vo.YolooEvent;
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
public class SearchQueueLeaser {
  private static final Logger LOGGER = LogManager.getLogger();

  private static final int NUMBER_OF_TASK_TO_LEASE = 100;

  private final List<TaskHandle> pendingTasksToDelete;
  private final Queue pullQueue;
  private final ObjectMapper objectMapper;
  private final EventHandler eventHandlerChain;

  public SearchQueueLeaser(
      @Qualifier(QueueBeanQualifier.SEARCH) Queue pullQueue,
      ObjectMapper objectMapper,
      EventHandler eventHandlerChain) {
    this.pendingTasksToDelete = new ArrayList<>();
    this.pullQueue = pullQueue;
    this.objectMapper = objectMapper;
    this.eventHandlerChain = eventHandlerChain;
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
        .leaseTasks(3600, TimeUnit.SECONDS, NUMBER_OF_TASK_TO_LEASE)
        .stream()
        .map(this::mapToEventState)
        .peek(SearchQueueLeaser::logState)
        .filter(state -> state instanceof EventState.Data)
        .map(state -> (EventState.Data) state)
        .peek(state -> pendingTasksToDelete.add(state.getTask()))
        .map(EventState.Data::getEvent)
        .collect(Collectors.groupingBy(event -> event.getMetadata().getType()))
        .forEach(eventHandlerChain::process);

    logStatus();

    pullQueue.deleteTaskAsync(pendingTasksToDelete);
  }

  private void logStatus() {
    int size = pendingTasksToDelete.size();
    if (size == 0) {
      LOGGER.info("Task Queue has no tasks available for lease.");
    } else {
      LOGGER.info("Processed and deleted '{}' tasks from the pull queue", size);
    }
  }

  private EventState mapToEventState(TaskHandle task) {
    try {
      return new EventState.Data(task, objectMapper.readValue(task.getPayload(), YolooEvent.class));
    } catch (IOException e) {
      return new EventState.Error(task, e);
    }
  }

  abstract static class EventState {
    abstract TaskHandle getTask();

    static final class Data extends EventState {
      private final TaskHandle task;
      private final YolooEvent event;

      Data(TaskHandle task, YolooEvent event) {
        this.task = task;
        this.event = event;
      }

      @Override
      TaskHandle getTask() {
        return task;
      }

      YolooEvent getEvent() {
        return event;
      }
    }

    static final class Error extends EventState {
      private final TaskHandle task;
      private final Throwable error;

      Error(TaskHandle task, Throwable error) {
        this.task = task;
        this.error = error;
      }

      @Override
      TaskHandle getTask() {
        return task;
      }

      Throwable getError() {
        return error;
      }
    }
  }
}
