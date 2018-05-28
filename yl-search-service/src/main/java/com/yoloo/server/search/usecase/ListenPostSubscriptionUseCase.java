package com.yoloo.server.search.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.repository.post.PostRepository;
import com.yoloo.server.search.vo.Message;
import com.yoloo.server.search.vo.PostResponse;
import com.yoloo.server.search.vo.PubSubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Lazy
@Component
public class ListenPostSubscriptionUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListenPostSubscriptionUseCase.class);

  private static final Set<String> CONSUMED_MESSAGE_IDS = new HashSet<>();

  private final PostRepository postRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public ListenPostSubscriptionUseCase(PostRepository postRepository, ObjectMapper objectMapper) {
    this.postRepository = postRepository;
    this.objectMapper = objectMapper;
  }

  public void execute(PubSubResponse response) {
    Message message = response.getMessage();
    String messageId = message.getMessageId();

    if (!CONSUMED_MESSAGE_IDS.contains(messageId)) {
      String jsonData = message.getJsonData();
      LOGGER.info("Json data: {}", jsonData);

      try {
        PostResponse postResponse = objectMapper.readValue(jsonData, PostResponse.class);
        LOGGER.info("Post Response: {}", postResponse);

        Post post =
            Post.newBuilder()
                .id(postResponse.getId())
                .title(postResponse.getTitle().getValue())
                .content(postResponse.getContent().getValue())
                .tags(
                    postResponse
                        .getTags()
                        .stream()
                        .map(PostResponse.Tag::getValue)
                        .collect(Collectors.toList()))
                .build();

        postRepository.save(post);
      } catch (IOException e) {
        LOGGER.error("Couldn't parse response", e);
      }

      CONSUMED_MESSAGE_IDS.add(messageId);
    }
  }
}
