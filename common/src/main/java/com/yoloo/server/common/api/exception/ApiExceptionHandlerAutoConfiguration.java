package com.yoloo.server.common.api.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiExceptionHandlerAutoConfiguration {

  @Bean
  public ApiExceptionHandler apiExceptionHandler(MessageSource messageSource) {
    return new ApiExceptionHandler(messageSource);
  }
}
