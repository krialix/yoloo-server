package com.yoloo.server.common.firebase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "firebase")
public class FirebaseYAMLConfig {

  private String databaseUrl;
  private String serviceAccountKeyName;

  public String getDatabaseUrl() {
    return databaseUrl;
  }

  public void setDatabaseUrl(String databaseUrl) {
    this.databaseUrl = databaseUrl;
  }

  public String getServiceAccountKeyName() {
    return serviceAccountKeyName;
  }

  public void setServiceAccountKeyName(String serviceAccountKeyName) {
    this.serviceAccountKeyName = serviceAccountKeyName;
  }
}
