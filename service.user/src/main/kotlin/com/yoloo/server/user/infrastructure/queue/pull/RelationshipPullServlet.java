package com.yoloo.server.user.infrastructure.queue.pull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.yoloo.server.user.infrastructure.eventlistener.RelationshipEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RelationshipPullServlet extends HttpServlet {

  private static final Logger log = LoggerFactory.getLogger(RelationshipPullServlet.class);
  private static final int NUMBER_OF_TASKS_TO_LEASE = 100;

  private final Queue queue;
  private final ObjectMapper mapper;

  public RelationshipPullServlet(
      @Qualifier("relationship-queue") Queue queue, ObjectMapper mapper) {
    this.queue = queue;
    this.mapper = mapper;
  }

  private static void processTasks(List<TaskHandle> tasks, Queue q, ObjectMapper mapper) {
    int numberOfDeletedTasks = 0;
    for (TaskHandle task : tasks) {
      RelationshipEventListener.Payload payload = null;
      try {
        payload = mapper.readValue(task.getPayload(), RelationshipEventListener.Payload.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      log.info("Processing: taskName='{}'  payload='{}'", task.getName(), payload);
      log.info("Deleting taskName='{}'", task.getName());

      q.deleteTask(task);
      numberOfDeletedTasks++;
    }

    final String message =
        numberOfDeletedTasks > 0
            ? "Processed and deleted "
                + NUMBER_OF_TASKS_TO_LEASE
                + " tasks from the "
                + " task queue."
            : "Task Queue has no tasks available for lease.";

    log.info(message);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    List<TaskHandle> tasks = queue.leaseTasks(3600, TimeUnit.SECONDS, NUMBER_OF_TASKS_TO_LEASE);

    processTasks(tasks, queue, mapper);
  }
}
