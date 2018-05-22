package com.yoloo.server.search.api;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

  private final SolrClient solrClient;

  public SearchController(SolrClient solrClient) {
    this.solrClient = solrClient;
  }

  @GetMapping
  public SolrDocumentList getDocuments() throws IOException, SolrServerException {
    SolrQuery solrQuery = new SolrQuery("*:*");

    return solrClient.query(solrQuery).getResults();
  }
}
