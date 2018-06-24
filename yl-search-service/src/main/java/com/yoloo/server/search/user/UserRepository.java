package com.yoloo.server.search.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends SolrCrudRepository<User, String> {

  Page<User> findUsersByDisplayNameContaining(String query, Pageable pageable);
}
