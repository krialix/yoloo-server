package com.yoloo.server;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DemoPullServlet extends HttpServlet {

  private static final Logger log = LoggerFactory.getLogger(DemoPullServlet.class);
  private static final int numberOfTasksToLease = 100;

  private static String message;

  private final Queue queue;

  public DemoPullServlet(Queue queue) {
    this.queue = queue;
  }

  private static String processTasks(List<TaskHandle> tasks, Queue q) {
    String payload;
    int numberOfDeletedTasks = 0;
    for (TaskHandle task : tasks) {
      payload = new String(task.getPayload());
      String output = String.format("Processing: taskName='%s'  payload='%s'", task.getName(), payload);
      log.info(output);
      output = String.format("Deleting taskName='%s'", task.getName());
      log.info(output);

      q.deleteTask(task);
      numberOfDeletedTasks++;
    }
    if (numberOfDeletedTasks > 0) {
      message =
          "Processed and deleted " + numberOfTasksToLease + " tasks from the " + " task queue.";
    } else {
      message = "Task Queue has no tasks available for lease.";
    }
    return message;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    log.info("---- PULL QUEUE IS CONSUMING ----");

    List<TaskHandle> tasks = queue.leaseTasks(3600, TimeUnit.SECONDS, 100);

    message = processTasks(tasks, queue);

    log.info(message);
  }
}
