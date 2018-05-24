package com.yoloo.server.search.repository.product;

import com.yoloo.server.search.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductFragment {

  Page<Product> search(String searchTerms, Pageable page);
}
