package com.yoloo.server.search.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.common.vo.PubSubMessage;
import com.yoloo.server.common.vo.PubSubResponse;
import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.repository.post.PostRepository;
import com.yoloo.server.search.usecase.PostCreatedEventUseCase.PostResponse.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostCreatedEventUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostCreatedEventUseCase.class);

  private static final Set<String> CONSUMED_MESSAGE_IDS = new HashSet<>();

  private final PostRepository postRepository;
  private final ObjectMapper objectMapper;

  public PostCreatedEventUseCase(PostRepository postRepository, ObjectMapper objectMapper) {
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

        Post post =
            Post.newBuilder()
                .id(postResponse.id)
                .title(postResponse.title.value)
                .content(postResponse.content.value)
                .tags(postResponse.tags.stream().map(Tag::getValue).collect(Collectors.toList()))
                .build();

        postRepository.save(post);
      } catch (IOException e) {
        LOGGER.error("Couldn't parse response", e);
      }

      CONSUMED_MESSAGE_IDS.add(messageId);
    }
  }

  static class PostResponse {

    String id;
    Title title;
    Content content;
    List<Tag> tags;

    private PostResponse() {
    }

    public String getId() {
      return id;
    }

    public Title getTitle() {
      return title;
    }

    public Content getContent() {
      return content;
    }

    public List<Tag> getTags() {
      return tags;
    }

    static class Title {

      String value;

      private Title() {
      }

      public String getValue() {
        return value;
      }
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

    static class BuddyRequest {

    }
  }
}
