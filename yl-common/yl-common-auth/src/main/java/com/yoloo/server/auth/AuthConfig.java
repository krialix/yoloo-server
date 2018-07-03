package com.yoloo.server.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.yoloo.server.auth.firebase.FirebaseAuthenticationProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.cache.NullUserCache;

@Configuration
public class AuthConfig {

  @ConditionalOnMissingBean
  @Bean
  public FirebaseAuthenticationProvider firebaseAuthenticationProvider(FirebaseAuth firebaseAuth) {
    FirebaseAuthenticationProvider provider = new FirebaseAuthenticationProvider(firebaseAuth);
    provider.setUserCache(new NullUserCache());
    return provider;
  }
}
