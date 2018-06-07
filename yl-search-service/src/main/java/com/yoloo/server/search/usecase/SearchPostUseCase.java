package com.yoloo.server.search.usecase;

import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.repository.post.PostRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.solr.core.query.SolrPageRequest;

public class SearchPostUseCase {

  private final PostRepository postRepository;

  public SearchPostUseCase(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Iterable<Post> execute(String query) {
    if (Strings.isBlank(query)) {
      return postRepository.findAll();
    }

    String[] values = query.split(" ");
    return postRepository.findPostsByTitleLikeOrTagsLikeOrContentLike(
        values, values, values, new SolrPageRequest(0, 30));
  }
}
