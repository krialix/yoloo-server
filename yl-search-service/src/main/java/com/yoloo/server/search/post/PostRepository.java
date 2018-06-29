package com.yoloo.server.search.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends SolrCrudRepository<Post, String> {

  @Query(value = "title:*?0*^2.0 OR tags:*?0*^1.5 OR content:*?0*")
  Page<Post> searchPost(String query, Pageable pageable);

  Page<Post> findPostsByTitleLikeOrTagsLikeOrContentLike(
      @Boost(2) String[] titleValue,
      @Boost(1.5f) String[] tagValue,
      String contentValue[],
      Pageable pageable);

  void deletePostsByIdIn(List<String> ids);
}
