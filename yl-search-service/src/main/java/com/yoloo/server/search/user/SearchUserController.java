package com.yoloo.server.search.user;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/search")
@RestController
public class SearchUserController {

  private final UserRepository userRepository;

  @Autowired
  public SearchUserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/users")
  public Iterable<User> searchUsers(@RequestParam(value = "q", required = false) String query) {
    if (Strings.isNullOrEmpty(query)) {
      return userRepository.findAll();
    }

    return userRepository.findUsersByDisplayNameContaining(query, new SolrPageRequest(0, 30));
  }
}
