package com.yoloo.server.search.post.handler;

import com.yoloo.server.common.queue.vo.EventType;
import com.yoloo.server.common.queue.vo.YolooEvent;
import com.yoloo.server.search.post.PostRepository;
import com.yoloo.server.search.queue.EventHandler;

import java.util.List;
import java.util.stream.Collectors;

public class DeletePostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public DeletePostEventHandler(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public void process(EventType eventType, List<YolooEvent> events) {
    if (eventType == EventType.DELETE_POST) {
      List<String> postIds =
          events
              .stream()
              .map(YolooEvent::getPayload)
              .map(map -> (String) map.get("id"))
              .collect(Collectors.toList());

      postRepository.deletePostsByIdIn(postIds);
    }

    processNext(eventType, events);
  }
}
