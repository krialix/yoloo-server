package com.yoloo.server.common.event;

import org.springframework.context.ApplicationEvent;

public class AppengineShutdownEvent extends ApplicationEvent {

  public AppengineShutdownEvent(Object source) {
    super(source);
  }
}
