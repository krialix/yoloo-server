package com.yoloo.server.search.repository.post;

import com.yoloo.server.search.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostFragment {

  Page<Post> search(String searchTerms, Pageable page);
}
