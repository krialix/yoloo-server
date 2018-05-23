package com.yoloo.server.search.repository;

import com.yoloo.server.search.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductFragmentImpl implements ProductFragment {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductFragmentImpl.class);

  private final SolrTemplate template;

  public ProductFragmentImpl(SolrTemplate template) {
    this.template = template;
  }

  @Override
  public List<Product> search(String searchTerm, Pageable page) {
    LOGGER.debug("Building a criteria query with search term: {} and page: {}", searchTerm, page);

    String[] words = searchTerm.split(" ");

    Criteria conditions = createSearchConditions(words);
    SimpleQuery search = new SimpleQuery(conditions);
    //search.setPageRequest(page);

    Page results = template.queryForPage("yoloo", search, Product.class);
    return results.getContent();
  }

  private Criteria createSearchConditions(String[] words) {
    Criteria conditions = null;

    for (String word: words) {
      if (conditions == null) {
        conditions = new Criteria("name").contains(word);
      }
    }

    return conditions;
  }
}
