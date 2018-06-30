package com.yoloo.server.search.queue;

import com.google.appengine.api.taskqueue.TaskHandle;
import com.yoloo.server.common.queue.vo.YolooEvent;

abstract class EventState {
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
