package com.yoloo.server.search.queue.handler;

import java.util.ArrayList;
import java.util.List;

public class EventHandlerChain {
  private final List<EventHandler> handlers;

  public EventHandlerChain() {
    this.handlers = new ArrayList<>();
  }

  public EventHandlerChain addChain(EventHandler handler) {
    this.handlers.add(handler);
    return this;
  }
}
