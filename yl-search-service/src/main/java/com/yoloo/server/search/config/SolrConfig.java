package com.yoloo.server.search.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class SolrConfig {

  @Bean
  public CommandLineRunner runner(SolrClient client) {
    return args -> {
      System.out.println("Hello 1");

      // create some test documents
      SolrInputDocument doc1 = new SolrInputDocument();
      doc1.addField("id", "1");

      SolrInputDocument doc2 = new SolrInputDocument();
      doc2.addField("id", "2");

      SolrInputDocument doc3 = new SolrInputDocument();
      doc3.addField("id", "3");

      SolrInputDocument doc4 = new SolrInputDocument();
      doc4.addField("id", "4");

      SolrInputDocument doc5 = new SolrInputDocument();
      doc5.addField("id", "5");
      try {
        client.add(Arrays.asList(doc1, doc2, doc3, doc4, doc5));
        client.commit();
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
      }
    };
  }
}
