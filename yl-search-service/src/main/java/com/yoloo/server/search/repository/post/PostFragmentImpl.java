package com.yoloo.server.search.repository.post;

import com.yoloo.server.search.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Crotch;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

@Repository
public class PostFragmentImpl implements PostFragment {

  private final SolrTemplate solrTemplate;

  @Autowired
  public PostFragmentImpl(SolrTemplate solrTemplate) {
    this.solrTemplate = solrTemplate;
  }

  @Override
  public Page<Post> search(String searchTerms, Pageable page) {
    Criteria criteria = buildSearchCriteria(searchTerms);
    SimpleQuery query = new SimpleQuery(criteria);
    return solrTemplate.queryForPage("posts", query, Post.class);
  }

  private Criteria buildSearchCriteria(String searchTerms) {
    String[] words = searchTerms.split(" ");

    Criteria criteria = null;
    for (String word : words) {
      criteria = criteria == null ? createQuery(word) : criteria.or(createQuery(word));
    }

    return criteria;
  }

  private Crotch createQuery(String word) {
    return new Criteria(Post.INDEX_TITLE)
        .contains(word)
        .boost(2.0f)
        .or(new Criteria(Post.INDEX_TAGS))
        .contains(word)
        .boost(1.5f)
        .or(new Criteria(Post.INDEX_CONTENT))
        .contains(word);
  }
}
