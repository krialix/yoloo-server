package com.yoloo.server.search.queue.handler;

import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewPostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public NewPostEventHandler(EventType eventType, PostRepository postRepository) {
    super(eventType);
    this.postRepository = postRepository;
  }

  @Override
  protected boolean isHandled(EventType eventType) {
    return eventType == EventType.NEW_POST;
  }

  @Override
  public void process(List<YolooEvent> events) {
    List<Post> posts =
        events
            .stream()
            .map(YolooEvent::getPayload)
            .map(this::createPost)
            .collect(Collectors.toList());

    postRepository.saveAll(posts);
  }

  private Post createPost(Map<String, Object> payload) {
    //noinspection unchecked
    return Post.newBuilder()
        .id((String) payload.get("id"))
        .title((String) payload.get("title"))
        .content((String) payload.get("content"))
        .tags((List<String>) payload.get("tags"))
        .build();
  }
}
