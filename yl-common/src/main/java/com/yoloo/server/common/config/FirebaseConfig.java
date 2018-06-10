package com.yoloo.server.common.config;

import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class FirebaseConfig {

  @Lazy
  @Bean
  public FirebaseMessaging firebaseMessaging() {
    return FirebaseMessaging.getInstance();
  }
}
