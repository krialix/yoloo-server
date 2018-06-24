package com.yoloo.server.search.post;

import com.yoloo.server.search.post.usecase.SearchPostUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/search")
@RestController
public class SearchPostController {

  private final SearchPostUseCase searchPostUseCase;

  @Autowired
  public SearchPostController(SearchPostUseCase searchPostUseCase) {
    this.searchPostUseCase = searchPostUseCase;
  }

  @GetMapping("/posts")
  public Iterable<Post> searchPosts(@RequestParam(value = "q", required = false) String query) {
    return searchPostUseCase.execute(query);
  }
}
