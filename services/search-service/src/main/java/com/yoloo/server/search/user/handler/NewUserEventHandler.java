package com.yoloo.server.search.user.handler;

import com.yoloo.server.search.event.Event;
import com.yoloo.server.search.event.EventHandler;
import com.yoloo.server.search.event.EventType;
import com.yoloo.server.search.user.User;
import com.yoloo.server.search.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewUserEventHandler extends EventHandler {
  private final UserRepository userRepository;

  public NewUserEventHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private static User createUser(Map<String, Object> payload) {
    return User.newBuilder()
        .id((String) payload.get("id"))
        .displayName((String) payload.get("displayName"))
        .build();
  }

  @Override
  protected boolean matches(EventType eventType) {
    return eventType == EventType.NEW_USER;
  }

  @Override
  protected void process(List<Event> events) {
    List<User> users =
        events
            .stream()
            .map(Event::getPayload)
            .map(NewUserEventHandler::createUser)
            .collect(Collectors.toList());

    userRepository.saveAll(users);
  }
}