package com.yoloo.server.common;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class RestConfig {

  @Bean
  public FilterRegistrationBean<ShallowEtagHeaderFilter> etagFilterRegistration() {
    FilterRegistrationBean<ShallowEtagHeaderFilter> bean =
        new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
    bean.addUrlPatterns("/api/posts/**", "/api/users/**");
    return bean;
  }

  @Lazy
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
