package com.yoloo.server.search.repository;

import com.yoloo.server.search.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductFragment {

  List<Product> search(String searchTerm, Pageable page);
}
