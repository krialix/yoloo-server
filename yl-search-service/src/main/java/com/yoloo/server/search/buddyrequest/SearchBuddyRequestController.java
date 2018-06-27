package com.yoloo.server.search.buddyrequest;

import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.usecase.SearchPostUseCase;
import org.springframework.web.bind.annotation.RequestParam;

// @RequestMapping("/api/search")
// @RestController
public class SearchBuddyRequestController {

  private final SearchPostUseCase searchPostUseCase;

  // @Autowired
  public SearchBuddyRequestController(SearchPostUseCase searchPostUseCase) {
    this.searchPostUseCase = searchPostUseCase;
  }

  // @GetMapping("/buddyrequests")
  public Iterable<Post> searchBuddyRequests(
      @RequestParam(value = "q", required = false) String query) {
    return searchPostUseCase.execute(query);
  }
}
