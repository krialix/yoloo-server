package com.yoloo.server.search.api;

import com.yoloo.server.search.entity.Post;
import com.yoloo.server.search.entity.Product;
import com.yoloo.server.search.repository.post.PostRepository;
import com.yoloo.server.search.repository.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

  private final PostRepository postRepository;
  private final ProductRepository productRepository;

  public SearchController(PostRepository postRepository, ProductRepository productRepository) {
    this.postRepository = postRepository;
    this.productRepository = productRepository;
  }

  @GetMapping("/posts/all")
  public Iterable<Post> listPosts() {
    return postRepository.findAll();
  }

  @GetMapping("/posts")
  public Page<Post> searchPost(@RequestParam("q") String query) {
    return postRepository.searchPost(query, new SolrPageRequest(0, 30));
  }

  @GetMapping
  public Iterable<Product> getDocuments() {
    return productRepository.findAll();
  }
}
