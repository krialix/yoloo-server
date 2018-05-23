package com.yoloo.server.search.config;

import com.yoloo.server.search.entity.Product;
import com.yoloo.server.search.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {

  @Bean
  public CommandLineRunner runner(ProductRepository repository) {
    return args -> {
      repository.deleteAll();

      // insert some products
      repository.save(new Product("1", "Nintendo Entertainment System", 11.5));
      repository.save(new Product("2", "Sega Megadrive", 1.0));
      repository.save(new Product("3", "Sony Playstation", 6.7));

      // fetch all
      System.out.println("Products found by findAll():");
      System.out.println("----------------------------");
      for (Product product : repository.findAll()) {
        System.out.println(product);
      }
      System.out.println();

      // fetch a single product
      System.out.println("Products found with findByNameStartingWith('So'):");
      System.out.println("--------------------------------");
      for (Product product : repository.findByNameStartsWith("So")) {
        System.out.println(product);
      }
      System.out.println();
    };
  }
}
