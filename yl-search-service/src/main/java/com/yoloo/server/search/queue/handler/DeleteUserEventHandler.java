package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.vo.EventType;
import com.yoloo.server.common.queue.vo.YolooEvent;
import com.yoloo.server.search.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteUserEventHandler extends EventHandler {
  private final UserRepository userRepository;

  public DeleteUserEventHandler(EventType eventType, UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @Override
  public void process(EventType eventType, List<YolooEvent> events) {
    if (eventType == EventType.DELETE_USER) {
      List<String> postIds =
          events
              .stream()
              .map(YolooEvent::getPayload)
              .map(map -> (String) map.get("id"))
              .collect(Collectors.toList());

      userRepository.deleteUsersByIdIn(postIds);
    }

    processNext(eventType, events);
  }
}
