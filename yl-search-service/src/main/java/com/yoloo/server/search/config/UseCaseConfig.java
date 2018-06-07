package com.yoloo.server.search.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.search.repository.post.PostRepository;
import com.yoloo.server.search.usecase.PostCreatedEventUseCase;
import com.yoloo.server.search.usecase.PostDeletedEventUseCase;
import com.yoloo.server.search.usecase.PostUpdatedEventUseCase;
import com.yoloo.server.search.usecase.SearchPostUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UseCaseConfig {

  @Lazy
  @Bean
  public SearchPostUseCase searchPostUseCase(PostRepository postRepository) {
    return new SearchPostUseCase(postRepository);
  }

  @Lazy
  @Bean
  public PostCreatedEventUseCase postCreatedEventUseCase(
      PostRepository postRepository, ObjectMapper mapper) {
    return new PostCreatedEventUseCase(postRepository, mapper);
  }

  @Lazy
  @Bean
  public PostUpdatedEventUseCase postUpdatedEventUseCase(
      PostRepository postRepository, ObjectMapper mapper) {
    return new PostUpdatedEventUseCase(postRepository, mapper);
  }

  @Lazy
  @Bean
  public PostDeletedEventUseCase postDeletedEventUseCase(PostRepository postRepository) {
    return new PostDeletedEventUseCase(postRepository);
  }
}
