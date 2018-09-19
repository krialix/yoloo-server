package com.yoloo.server.objectify;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "objectify")
public class ObjectifyYamlProperties {
  private boolean enableDatabaseRequestTracking = false;

  public boolean getEnableDatabaseRequestTracking() {
    return enableDatabaseRequestTracking;
  }

  public void setEnableDatabaseRequestTracking(boolean enableDatabaseRequestTracking) {
    this.enableDatabaseRequestTracking = enableDatabaseRequestTracking;
  }
}
