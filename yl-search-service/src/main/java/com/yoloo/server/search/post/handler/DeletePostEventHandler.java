package com.yoloo.server.search.post.handler;

import com.yoloo.server.search.event.Event;
import com.yoloo.server.search.event.EventHandler;
import com.yoloo.server.search.event.EventType;
import com.yoloo.server.search.post.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeletePostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public DeletePostEventHandler(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  protected boolean matches(EventType eventType) {
    return eventType == EventType.DELETE_POST;
  }

  @Override
  protected void process(List<Event> events) {
    List<String> postIds =
        events
            .stream()
            .map(Event::getPayload)
            .map(map -> (String) map.get("id"))
            .collect(Collectors.toList());

    postRepository.deletePostsByIdIn(postIds);
  }
}
