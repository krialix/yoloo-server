package com.yoloo.server.search.api;

import com.yoloo.server.search.entity.Product;
import com.yoloo.server.search.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

  private final ProductRepository repository;

  public SearchController(ProductRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public Iterable<Product> getDocuments() {
    return repository.findAll();
  }
}
