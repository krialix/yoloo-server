package com.yoloo.spring.autoconfiguration.firebase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
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
