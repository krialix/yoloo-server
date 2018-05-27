package com.yoloo.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Order(99)
@EnableWebSecurity
@Configuration
public class SecurityConfigurationSupport extends WebSecurityConfigurerAdapter {

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler(ObjectMapper objectMapper) {
    return new DefaultAuthenticationFailureHandler(objectMapper);
  }
}
