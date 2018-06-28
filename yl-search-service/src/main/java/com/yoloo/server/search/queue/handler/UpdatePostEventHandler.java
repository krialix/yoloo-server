package com.yoloo.server.search.queue.handler;

import com.google.common.collect.Lists;
import com.yoloo.server.common.queue.api.EventType;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdatePostEventHandler extends EventHandler {
  private final PostRepository postRepository;

  public UpdatePostEventHandler(EventType eventType, PostRepository postRepository) {
    super(eventType);
    this.postRepository = postRepository;
  }

  @Override
  protected boolean isHandled(EventType eventType) {
    return eventType == EventType.UPDATE_POST;
  }

  @Override
  public void process(List<YolooEvent> events) {
    Map<String, Post> posts =
        events
            .stream()
            .map(YolooEvent::getPayload)
            .map(map -> (String) map.get("id"))
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    ids ->
                        Lists.newArrayList(postRepository.findAllById(ids))
                            .stream()
                            .collect(Collectors.toMap(Post::getId, post -> post))));

    List<Post> updatedPosts = events
        .stream()
        .map(YolooEvent::getPayload)
        .map(map -> {
          String id = (String) map.get("id");
          return posts.computeIfPresent(id, (s, post) -> updatePost(post, map));
        })
        .collect(Collectors.toList());

    postRepository.saveAll(updatedPosts);
  }

  private Post updatePost(Post post, Map<String, Object> payload) {
    //noinspection unchecked
    return post.toBuilder()
        .title((String) payload.get("title"))
        .content((String) payload.get("content"))
        .tags((List<String>) payload.get("tags"))
        .build();
  }
}
