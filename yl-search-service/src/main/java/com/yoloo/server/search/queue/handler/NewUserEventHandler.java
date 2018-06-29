package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.search.user.User;
import com.yoloo.server.search.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewUserEventHandler extends EventHandler {
  private final UserRepository userRepository;

  public NewUserEventHandler(EventType eventType, UserRepository userRepository) {
    super(eventType);
    this.userRepository = userRepository;
  }

  private static User createUser(Map<String, Object> payload) {
    return User.newBuilder()
        .id((String) payload.get("id"))
        .displayName((String) payload.get("displayName"))
        .build();
  }

  @Override
  protected boolean isHandled(EventType eventType) {
    return eventType == EventType.NEW_USER;
  }

  @Override
  public void process(List<YolooEvent> events) {
    List<User> users =
        events
            .stream()
            .map(YolooEvent::getPayload)
            .map(NewUserEventHandler::createUser)
            .collect(Collectors.toList());

    userRepository.saveAll(users);
  }
}
