package com.yoloo.server.auth.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth")
public class ScopeProperties {
  private List<String> scopes;

  public List<String> getScopes() {
    return scopes;
  }

  public String[] getScopesArray() {
    return getScopes().toArray(new String[0]);
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }
}
