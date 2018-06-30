package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.vo.EventType;
import com.yoloo.server.common.queue.vo.YolooEvent;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewPostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public NewPostEventHandler(EventType eventType, PostRepository postRepository) {
    super();
    this.postRepository = postRepository;
  }

  private static Post createPost(Map<String, Object> payload) {
    //noinspection unchecked
    return Post.newBuilder()
        .id((String) payload.get("id"))
        .title((String) payload.get("title"))
        .content((String) payload.get("content"))
        .tags((List<String>) payload.get("tags"))
        .build();
  }

  @Override
  public void process(EventType eventType, List<YolooEvent> events) {
    if (eventType == EventType.NEW_POST) {
      List<Post> posts =
          events
              .stream()
              .map(YolooEvent::getPayload)
              .map(NewPostEventHandler::createPost)
              .collect(Collectors.toList());

      postRepository.saveAll(posts);
    }

    processNext(eventType, events);
  }
}
