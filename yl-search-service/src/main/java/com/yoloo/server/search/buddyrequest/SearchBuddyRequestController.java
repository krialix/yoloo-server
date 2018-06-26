package com.yoloo.server.search.buddyrequest;

import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.usecase.SearchPostUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/search")
@RestController
public class SearchBuddyRequestController {

  private final SearchPostUseCase searchPostUseCase;

  @Autowired
  public SearchBuddyRequestController(SearchPostUseCase searchPostUseCase) {
    this.searchPostUseCase = searchPostUseCase;
  }

  @GetMapping("/buddyrequests")
  public Iterable<Post> searchBuddyRequests(@RequestParam(value = "q", required = false) String query) {
    return searchPostUseCase.execute(query);
  }
}
