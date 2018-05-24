package com.yoloo.server.search.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.entity.Product;
import com.yoloo.server.search.repository.post.PostRepository;
import com.yoloo.server.search.repository.product.ProductRepository;
import com.yoloo.server.search.util.PostUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
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

  @Bean
  public CommandLineRunner runner(ProductRepository productRepository, PostRepository postRepository, ObjectMapper mapper) {
    return args -> {
      productRepository.deleteAll();
      postRepository.deleteAll();

      File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "posts.json");
      List<Post> posts = PostUtil.extractPosts(mapper, file);
      postRepository.saveAll(posts);

      // insert some products
      productRepository.save(new Product("1", "Nintendo Entertainment System", 11.5));
      productRepository.save(new Product("2", "Sega Megadrive", 1.0));
      productRepository.save(new Product("3", "Sony Playstation", 6.7));

      // fetch all
      System.out.println("Products found by findAll():");
      System.out.println("----------------------------");
      for (Product product : productRepository.findAll()) {
        System.out.println(product);
      }
      System.out.println();

      // fetch a single product
      System.out.println("Products found with findByNameStartingWith('So'):");
      System.out.println("--------------------------------");
      for (Product product : productRepository.search("So", Pageable.unpaged())) {
        System.out.println(product);
      }
      System.out.println();
    };
  }
}
