package com.yoloo.server.search.solr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;
import com.yoloo.server.search.post.JsonUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactory;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class SolrConfig {

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
    EmbeddedSolrServerFactory factory =
        new EmbeddedSolrServerFactory(ResourceUtils.CLASSPATH_URL_PREFIX + "solr");
    return factory.getSolrClient();
  }

  @Bean
  public SolrTemplate solrTemplate(SolrClient client) {
    return new SolrTemplate(client);
  }
}