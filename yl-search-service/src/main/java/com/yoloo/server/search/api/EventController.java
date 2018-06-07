package com.yoloo.server.search.api;

import com.yoloo.server.common.util.PubSubHelper;
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

@RequestMapping("/api/search/events")
@RestController
public class EventController {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

  private final PostCreatedEventUseCase postCreatedEventUseCase;
  private final PostUpdatedEventUseCase postUpdatedEventUseCase;
  private final PostDeletedEventUseCase postDeletedEventUseCase;

  @Autowired
  public EventController(
      PostCreatedEventUseCase postCreatedEventUseCase,
      PostUpdatedEventUseCase postUpdatedEventUseCase,
      PostDeletedEventUseCase postDeletedEventUseCase) {
    this.postCreatedEventUseCase = postCreatedEventUseCase;
    this.postUpdatedEventUseCase = postUpdatedEventUseCase;
    this.postDeletedEventUseCase = postDeletedEventUseCase;
  }

  @PostMapping("/post.create")
  public void postCreatedEvent(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = PubSubHelper.convertToPubSubResponse(request);
      postCreatedEventUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }

  @PostMapping("/post.update")
  public void postUpdatedEvent(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = PubSubHelper.convertToPubSubResponse(request);
      postUpdatedEventUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }

  @PostMapping("/post.delete")
  public void postDeletedEvent(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = PubSubHelper.convertToPubSubResponse(request);
      postDeletedEventUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }
}