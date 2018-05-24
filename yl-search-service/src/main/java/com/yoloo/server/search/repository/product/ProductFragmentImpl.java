package com.yoloo.server.search.repository.product;

import com.yoloo.server.search.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ProductFragmentImpl implements ProductFragment {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductFragmentImpl.class);

  private final SolrTemplate template;

  public ProductFragmentImpl(SolrTemplate template) {
    this.template = template;
  }

  @Override
  public Page<Product> search(String searchTerms, Pageable page) {
    LOGGER.debug("Building a criteria query with search term: {} and page: {}", searchTerms, page);

    Criteria conditions = buildSearchCriteria(searchTerms);
    SimpleQuery query = new SimpleQuery(conditions);
    //search.setPageRequest(page);

    return template.queryForPage("yoloo", query, Product.class);
  }

  private Criteria buildSearchCriteria(String searchTerms) {
    String[] words = searchTerms.split(" ");

    /*Criteria criteria = new Criteria();
    for (String word: words) {
      criteria = criteria.and(new Criteria("name").contains(word));
    }

    return criteria;*/

    Criteria conditions = null;
    for (String word: words) {
      if (conditions == null) {
        conditions = new Criteria("name").contains(word);
      }
    }

    return conditions;
  }
}
