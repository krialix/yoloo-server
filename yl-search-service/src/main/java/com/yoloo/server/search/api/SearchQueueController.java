package com.yoloo.server.search.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.common.queue.config.QueueEndpoint;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping(QueueEndpoint.QUEUE_SEARCH_ENDPOINT)
@RestController
public class SearchQueueController {

  private final ObjectMapper mapper;
  private final PostRepository postRepository;

  public SearchQueueController(ObjectMapper mapper, PostRepository postRepository) {
    this.mapper = mapper;
    this.postRepository = postRepository;
  }

  @PostMapping
  public void processSearchQueue(HttpServletRequest request) throws IOException {
    String json = request.getParameter("data");
    YolooEvent event = mapper.readValue(json, YolooEvent.class);

    switch (event.getMetadata().getType()) {
      case NEW_POST:
        createPost(event.getPayload());
        break;
      case UPDATE_POST:
        updatePost(event.getPayload());
        break;
      case DELETE_POST:
        deletePost(event.getPayload());
        break;
      case NEW_USER:
        break;
      case UPDATE_USER:
        break;
      case DELETE_USER:
        break;
    }
  }

  private void deletePost(Map<String, Object> payload) {
    postRepository.deleteById((String) payload.get("id"));
  }

  private void createPost(Map<String, Object> payload) {
    //noinspection unchecked
    Post post =
        Post.newBuilder()
            .id((String) payload.get("id"))
            .title((String) payload.get("title"))
            .content((String) payload.get("content"))
            .tags((List<String>) payload.get("tags"))
            .build();

    postRepository.save(post);
  }

  private void updatePost(Map<String, Object> payload) {
    //noinspection unchecked
    postRepository
        .findById((String) payload.get("id"))
        .map(
            post ->
                post.toBuilder()
                    .content((String) payload.get("content"))
                    .tags((List<String>) payload.get("tags"))
                    .build())
        .ifPresent(postRepository::save);
  }
}
