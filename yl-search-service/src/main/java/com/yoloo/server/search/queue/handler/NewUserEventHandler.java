package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.vo.EventType;
import com.yoloo.server.common.queue.vo.YolooEvent;
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
  public void process(EventType eventType, List<YolooEvent> events) {
    if (eventType == EventType.NEW_USER) {
      List<User> users =
          events
              .stream()
              .map(YolooEvent::getPayload)
              .map(NewUserEventHandler::createUser)
              .collect(Collectors.toList());

      userRepository.saveAll(users);
    }

    processNext(eventType, events);
  }
}
