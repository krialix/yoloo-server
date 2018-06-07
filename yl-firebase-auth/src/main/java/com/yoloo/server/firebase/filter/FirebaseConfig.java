package com.yoloo.server.firebase.filter;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

  @Bean
  public FirebaseAuth firebaseAuth() {
    return FirebaseAuth.getInstance();
  }
}
