package com.yoloo.server.search.user.handler;

import com.google.common.collect.Lists;
import com.yoloo.server.search.event.Event;
import com.yoloo.server.search.event.EventHandler;
import com.yoloo.server.search.event.EventType;
import com.yoloo.server.search.user.User;
import com.yoloo.server.search.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateUserEventHandler extends EventHandler {
  private final UserRepository userRepository;

  public UpdateUserEventHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private static User updateUser(User user, Map<String, Object> payload) {
    return user.toBuilder().displayName((String) payload.get("displayName")).build();
  }

  @Override
  protected boolean matches(EventType eventType) {
    return eventType == EventType.UPDATE_USER;
  }

  @Override
  protected void process(List<Event> events) {
    Map<String, User> users =
        events
            .stream()
            .map(Event::getPayload)
            .map(map -> (String) map.get("id"))
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    ids ->
                        Lists.newArrayList(userRepository.findAllById(ids))
                            .stream()
                            .collect(Collectors.toMap(User::getId, post -> post))));

    List<User> updatedUsers =
        events
            .stream()
            .map(Event::getPayload)
            .map(
                map -> {
                  String id = (String) map.get("id");
                  return users.computeIfPresent(id, (s, user) -> updateUser(user, map));
                })
            .collect(Collectors.toList());

    userRepository.saveAll(updatedUsers);
  }
}
