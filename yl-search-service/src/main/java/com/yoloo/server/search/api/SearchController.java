package com.yoloo.server.search.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.usecase.ListenPostSubscriptionUseCase;
import com.yoloo.server.search.usecase.SearchPostUseCase;
import com.yoloo.server.search.vo.PubSubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

  private final ObjectMapper objectMapper;
  private final ListenPostSubscriptionUseCase listenPostSubscriptionUseCase;
  private final SearchPostUseCase searchPostUseCase;

  @Autowired
  public SearchController(
      ObjectMapper objectMapper,
      ListenPostSubscriptionUseCase listenPostSubscriptionUseCase,
      SearchPostUseCase searchPostUseCase) {
    this.objectMapper = objectMapper;
    this.listenPostSubscriptionUseCase = listenPostSubscriptionUseCase;
    this.searchPostUseCase = searchPostUseCase;
  }

  @PostMapping("/subscription/post")
  public void listenPostSubscription(HttpServletRequest request) {
    try {
      PubSubResponse pubSubResponse = objectMapper.readValue(request.getInputStream(), PubSubResponse.class);
      listenPostSubscriptionUseCase.execute(pubSubResponse);
    } catch (IOException e) {
      LOGGER.error("Couldn't parse response", e);
    }
  }

  @GetMapping("/posts")
  public Iterable<Post> searchPost(@RequestParam(value = "q", required = false) String query) {
    return searchPostUseCase.execute(query);
  }
}
