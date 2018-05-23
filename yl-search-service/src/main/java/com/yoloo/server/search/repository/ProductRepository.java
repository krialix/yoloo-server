package com.yoloo.server.search.repository;

import com.yoloo.server.search.entity.Product;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface ProductRepository extends SolrCrudRepository<Product, String> {

  List<Product> findByNameStartsWith(String name);
}
