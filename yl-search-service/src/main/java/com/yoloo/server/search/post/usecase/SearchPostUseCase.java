package com.yoloo.server.search.post.usecase;

import com.google.common.base.Strings;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;
import org.springframework.data.solr.core.query.SolrPageRequest;

public class SearchPostUseCase {

  private final PostRepository postRepository;

  public SearchPostUseCase(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Iterable<Post> execute(String query) {
    if (Strings.isNullOrEmpty(query)) {
      return postRepository.findAll();
    }

    String[] values = query.split(" ");
    return postRepository.findPostsByTitleLikeOrTagsLikeOrContentLike(
        values, values, values, new SolrPageRequest(0, 30));
  }
}
