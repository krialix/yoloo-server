package com.yoloo.server.search.post.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.common.vo.PubSubMessage;
import com.yoloo.server.common.vo.PubSubResponse;
import com.yoloo.server.search.post.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostUpdatedEventUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostUpdatedEventUseCase.class);

  private static final Set<String> CONSUMED_MESSAGE_IDS = new HashSet<>();

  private final PostRepository postRepository;
  private final ObjectMapper objectMapper;

  public PostUpdatedEventUseCase(PostRepository postRepository, ObjectMapper objectMapper) {
    this.postRepository = postRepository;
    this.objectMapper = objectMapper;
  }

  public void execute(PubSubResponse response) {
    PubSubMessage message = response.getMessage();
    String messageId = message.getMessageId();

    if (!CONSUMED_MESSAGE_IDS.contains(messageId)) {
      String jsonData = message.getJsonData();
      LOGGER.info("Json data: {}", jsonData);

      try {
        PostResponse postResponse = objectMapper.readValue(jsonData, PostResponse.class);
        LOGGER.info("Post Response: {}", postResponse);

        postRepository.findById(postResponse.id)
            .map(
                post -> post.toBuilder()
                    .content(postResponse.content.value)
                    .tags(postResponse.tags
                        .stream()
                        .map(PostResponse.Tag::getValue)
                        .collect(Collectors.toList()))
                    .build())
            .ifPresent(postRepository::save);
      } catch (IOException e) {
        LOGGER.error("Couldn't parse response", e);
      }

      CONSUMED_MESSAGE_IDS.add(messageId);
    }
  }

  static class PostResponse {

    String id;
    Content content;
    List<Tag> tags;

    private PostResponse() {
    }

    public String getId() {
      return id;
    }

    public Content getContent() {
      return content;
    }

    public List<Tag> getTags() {
      return tags;
    }

    static class Content {

      String value;

      private Content() {
      }

      public String getValue() {
        return value;
      }
    }

    static class Tag {

      String value;

      private Tag() {
      }

      public String getValue() {
        return value;
      }
    }
  }
}
