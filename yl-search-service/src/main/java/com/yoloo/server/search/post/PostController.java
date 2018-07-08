package com.yoloo.server.search.post;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/search")
@RestController
class PostController {

  private final PostRepository postRepository;

  @Autowired
  PostController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping("/posts")
  Iterable<Post> searchPosts(@RequestParam(value = "q", required = false) String query) {
    if (Strings.isNullOrEmpty(query)) {
      return postRepository.findAll();
    }

    String[] values = query.split(" ");
    return postRepository.findPostsByTitleLikeOrTagsLikeOrContentLike(
        values, values, values, new SolrPageRequest(0, 30));
  }
}
