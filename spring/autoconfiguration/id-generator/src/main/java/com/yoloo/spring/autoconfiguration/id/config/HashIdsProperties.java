package com.yoloo.spring.autoconfiguration.id.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hashids")
public class HashIdsProperties {
  private String alphabet;
  private String salt;
  private int minLength;

  public String getAlphabet() {
    return alphabet;
  }

  public void setAlphabet(String alphabet) {
    this.alphabet = alphabet;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public int getMinLength() {
    return minLength;
  }

  public void setMinLength(int minLength) {
    this.minLength = minLength;
  }
}
