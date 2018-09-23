package com.yoloo.server.search.post.handler;

import com.yoloo.server.search.event.Event;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;
import com.yoloo.server.search.event.EventHandler;
import com.yoloo.server.search.event.EventType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewPostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public NewPostEventHandler(PostRepository postRepository) {
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
  protected boolean matches(EventType eventType) {
    return eventType == EventType.NEW_POST;
  }

  @Override
  protected void process(List<Event> events) {
    List<Post> posts =
        events
            .stream()
            .map(Event::getPayload)
            .map(NewPostEventHandler::createPost)
            .collect(Collectors.toList());

    postRepository.saveAll(posts);
  }
}