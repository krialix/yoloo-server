package com.yoloo.server.search.repository.product;

import com.yoloo.server.search.entity.Product;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends SolrCrudRepository<Product, String>, ProductFragment {

}
