package com.yoloo.server.search.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/search")
@RestController
class UserController {

  private final UserRepository userRepository;

  @Autowired
  UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/users")
  Iterable<User> searchUsers(@RequestParam(value = "q", required = false) String query) {
    return userRepository.findUsersByDisplayName(query, new SolrPageRequest(0, 30));
  }
}
