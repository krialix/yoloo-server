package com.yoloo.server.search.repository.post;

import com.yoloo.server.search.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends SolrCrudRepository<Post, String>, PostFragment {

  @Query("title:*(?0)*^2.0 OR tags:*(?0)*^1.5 OR content:*(?0)*")
  Page<Post> searchPost(String query, Pageable pageable);
}
