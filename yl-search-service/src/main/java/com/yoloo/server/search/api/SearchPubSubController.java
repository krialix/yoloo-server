package com.yoloo.server.search.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.common.vo.PubSubResponse;
import com.yoloo.server.search.usecase.PostCreatedEventUseCase;
import com.yoloo.server.search.usecase.PostDeletedEventUseCase;
import com.yoloo.server.search.usecase.PostUpdatedEventUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping("/api/search/pubsub")
@RestController
public class SearchPubSubController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchPubSubController.class);

  private final PostCreatedEventUseCase postCreatedEventUseCase;
  private final PostUpdatedEventUseCase postUpdatedEventUseCase;
  private final PostDeletedEventUseCase postDeletedEventUseCase;
  private final ObjectMapper mapper;

  @Autowired
  public SearchPubSubController(
      PostCreatedEventUseCase postCreatedEventUseCase,
      PostUpdatedEventUseCase postUpdatedEventUseCase,
      PostDeletedEventUseCase postDeletedEventUseCase,
      ObjectMapper mapper) {
    this.postCreatedEventUseCase = postCreatedEventUseCase;
    this.postUpdatedEventUseCase = postUpdatedEventUseCase;
    this.postDeletedEventUseCase = postDeletedEventUseCase;
    this.mapper = mapper;
  }

  @PostMapping("/post.create")
  public void postCreatedEvent(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = PubSubResponse.from(mapper, request.getInputStream());
      postCreatedEventUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }

  @PostMapping("/post.update")
  public void postUpdatedEvent(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = PubSubResponse.from(mapper, request.getInputStream());
      postUpdatedEventUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }

  @PostMapping("/post.delete")
  public void postDeletedEvent(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = PubSubResponse.from(mapper, request.getInputStream());
      postDeletedEventUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }
}
