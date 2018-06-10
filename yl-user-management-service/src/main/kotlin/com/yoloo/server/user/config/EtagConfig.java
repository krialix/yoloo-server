package com.yoloo.server.user.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagConfig {

  @Bean
  public FilterRegistrationBean<ShallowEtagHeaderFilter> etagFilterRegistration() {
    FilterRegistrationBean<ShallowEtagHeaderFilter> bean =
        new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
    bean.addUrlPatterns("/api/");
    return bean;
  }
}
