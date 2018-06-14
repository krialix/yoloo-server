package com.yoloo.server.common.appengine.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class FirebaseConfig {

  @Lazy
  @Bean
  public FirebaseAuth firebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Lazy
  @Bean
  public FirebaseMessaging firebaseMessaging() {
    return FirebaseMessaging.getInstance();
  }
}
