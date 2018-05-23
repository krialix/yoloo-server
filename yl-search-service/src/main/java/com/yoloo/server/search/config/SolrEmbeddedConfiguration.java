package com.yoloo.server.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;
import org.springframework.util.ResourceUtils;

@Configuration
public class SolrEmbeddedConfiguration {

  /*@Bean
  public SolrClient solrClient() throws IOException {
    Path solrHome =
        Paths.get(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "solr").getPath());
    Path solrConfig = new File(solrHome + "/solr.xml").toPath();

    CoreContainer container = CoreContainer.createAndLoad(solrHome, solrConfig);
    container.load();

    return new EmbeddedSolrServer(container, DEFAULT_CORE_NAME);
  }*/

  @Bean
  public EmbeddedSolrServerFactoryBean solrServerFactoryBean() {
    EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
    factory.setSolrHome(ResourceUtils.CLASSPATH_URL_PREFIX + "solr");
    return factory;
  }

  @Bean
  public SolrTemplate solrTemplate() throws Exception {
    return new SolrTemplate(solrServerFactoryBean().getObject());
  }
}
