package com.yoloo.server.search.event;

import java.util.List;

public abstract class EventHandler {

  private EventHandler next;

  public EventHandler setNext(EventHandler next) {
    this.next = next;
    return this;
  }

  protected abstract boolean matches(EventType eventType);

  protected abstract void process(List<Event> events);

  public void process(EventType eventType, List<Event> events) {
    if (matches(eventType)) {
      process(events);
    } else {
      processNext(eventType, events);
    }
  }

  private void processNext(EventType eventType, List<Event> events) {
    if (next != null) {
      next.process(eventType, events);
    }
  }
}
