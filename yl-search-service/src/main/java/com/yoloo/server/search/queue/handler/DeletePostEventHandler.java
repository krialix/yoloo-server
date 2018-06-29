package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.search.post.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeletePostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public DeletePostEventHandler(EventType eventType, PostRepository postRepository) {
    super(eventType);
    this.postRepository = postRepository;
  }

  @Override
  protected boolean isHandled(EventType eventType) {
    return eventType == EventType.DELETE_POST;
  }

  @Override
  public void process(List<YolooEvent> events) {
    List<String> postIds =
        events
            .stream()
            .map(YolooEvent::getPayload)
            .map(map -> (String) map.get("id"))
            .collect(Collectors.toList());

    postRepository.deletePostsByIdIn(postIds);
  }
}
