package com.yoloo.server.search.config;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactory;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

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
  public SolrClient solrClient() throws IOException, ParserConfigurationException, SAXException {
    return new EmbeddedSolrServerFactory(ResourceUtils.CLASSPATH_URL_PREFIX + "solr")
        .getSolrClient();
  }

  @Bean
  public SolrTemplate solrTemplate(SolrClient client) {
    return new SolrTemplate(client);
  }
}
