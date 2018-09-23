package com.yoloo.server.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.search.post.JsonUtil;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;
import com.yoloo.server.search.user.User;
import com.yoloo.server.search.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

@EnableSolrRepositories
@SpringBootApplication
public class SearchApplication {

  public static void main(String[] args) {
    SpringApplication.run(SearchApplication.class, args);
  }

  @Profile("dev")
  @Bean
  CommandLineRunner commandLineRunner(ObjectMapper objectMapper, UserRepository repository) {
    return args -> {
      repository.deleteAll();

      File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "users.json");
      List<User> users = JsonUtil.toList(objectMapper, file);
      repository.saveAll(users);
    };
  }

  @Profile("dev")
  @Bean
  public CommandLineRunner runner(PostRepository postRepository, ObjectMapper mapper) {
    return args -> {
      postRepository.deleteAll();

      File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "posts.json");
      List<Post> posts = JsonUtil.toList(mapper, file);
      postRepository.saveAll(posts);
    };
  }
}
