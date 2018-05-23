package com.yoloo.server.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@EnableSolrRepositories
@SpringBootApplication
public class SearchApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(SearchApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(SearchApplication.class);
  }
}
