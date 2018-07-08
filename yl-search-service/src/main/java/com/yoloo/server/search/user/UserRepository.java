package com.yoloo.server.search.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface UserRepository extends SolrCrudRepository<User, String> {

  public Page<User> findUsersByDisplayNameContaining(String query, Pageable pageable);

  public void deleteUsersByIdIn(List<String> ids);
}
