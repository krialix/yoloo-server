package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.search.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteUserEventHandler extends EventHandler {
  private final UserRepository userRepository;

  public DeleteUserEventHandler(EventType eventType, UserRepository userRepository) {
    super(eventType);
    this.userRepository = userRepository;
  }

  @Override
  protected boolean isHandled(EventType eventType) {
    return eventType == EventType.DELETE_USER;
  }

  @Override
  public void process(List<YolooEvent> events) {
    List<String> postIds =
        events
            .stream()
            .map(YolooEvent::getPayload)
            .map(map -> (String) map.get("id"))
            .collect(Collectors.toList());

    userRepository.deleteUsersByIdIn(postIds);
  }
}
