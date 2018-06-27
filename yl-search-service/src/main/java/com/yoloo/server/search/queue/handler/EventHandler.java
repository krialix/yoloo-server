package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;

import java.util.List;

public abstract class EventHandler {

  private final EventType eventType;

  private EventHandler eventHandler;

  public EventHandler(EventType eventType) {
    this.eventType = eventType;
  }

  public void setNextEventHandler(EventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  protected abstract boolean isHandled(EventType eventType);

  private void processNext(List<YolooEvent> events) {
    if (eventHandler != null && isHandled(eventType)) {
      eventHandler.process(events);
    }
  }

  public abstract void process(List<YolooEvent> events);
}
