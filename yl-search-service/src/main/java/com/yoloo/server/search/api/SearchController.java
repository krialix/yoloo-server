package com.yoloo.server.search.api;

import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.usecase.SearchPostUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/search")
@RestController
public class SearchController {

  private final SearchPostUseCase searchPostUseCase;

  @Autowired
  public SearchController(SearchPostUseCase searchPostUseCase) {
    this.searchPostUseCase = searchPostUseCase;
  }

  @GetMapping("/posts")
  public Iterable<Post> searchPosts(@RequestParam(value = "q", required = false) String query) {
    return searchPostUseCase.execute(query);
  }
}
