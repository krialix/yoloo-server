package com.yoloo.server.search.api;

import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.repository.post.PostRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

  private final PostRepository postRepository;

  @Autowired
  public SearchController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping("/posts")
  public Iterable<Post> searchPost(@RequestParam(value = "q", required = false) String query) {
    if (Strings.isBlank(query)) {
      return postRepository.findAll();
    }

    String[] values = query.split(" ");
    return postRepository.findPostsByTitleLikeOrTagsLikeOrContentLike(
        values, values, values, new SolrPageRequest(0, 30));
  }
}
