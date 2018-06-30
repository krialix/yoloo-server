package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.vo.EventType;
import com.yoloo.server.common.queue.vo.YolooEvent;

import java.util.List;

public abstract class EventHandler {

  private EventHandler next;

  public EventHandler setNext(EventHandler next) {
    this.next = next;
    return this;
  }

  public abstract void process(EventType eventType, List<YolooEvent> events);

  protected void processNext(EventType eventType, List<YolooEvent> events) {
    if (next != null) {
      next.process(eventType, events);
    }
  }
}
