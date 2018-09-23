package com.yoloo.server.search.user.handler;

import com.yoloo.server.search.event.EventHandler;
import com.yoloo.server.search.event.EventType;
import com.yoloo.server.search.event.Event;
import com.yoloo.server.search.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteUserEventHandler extends EventHandler {
  private final UserRepository userRepository;

  public DeleteUserEventHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  protected boolean matches(EventType eventType) {
    return eventType == EventType.DELETE_USER;
  }

  @Override
  protected void process(List<Event> events) {
    List<String> postIds =
        events
            .stream()
            .map(Event::getPayload)
            .map(map -> (String) map.get("id"))
            .collect(Collectors.toList());

    userRepository.deleteUsersByIdIn(postIds);
  }
}