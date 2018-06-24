package com.yoloo.server.search.post.usecase;

import com.yoloo.server.common.vo.PubSubMessage;
import com.yoloo.server.common.vo.PubSubResponse;
import com.yoloo.server.search.post.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class PostDeletedEventUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostDeletedEventUseCase.class);

  private static final Set<String> CONSUMED_MESSAGE_IDS = new HashSet<>();

  private final PostRepository postRepository;

  public PostDeletedEventUseCase(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public void execute(PubSubResponse response) {
    PubSubMessage message = response.getMessage();
    String messageId = message.getMessageId();

    if (!CONSUMED_MESSAGE_IDS.contains(messageId)) {
      String postId = message.getData();

      postRepository.deleteById(postId);
      LOGGER.info("post id: {} is deleted", postId);

      CONSUMED_MESSAGE_IDS.add(messageId);
    }
  }
}
