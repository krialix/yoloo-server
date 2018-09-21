package com.yoloo.spring.autoconfiguration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Configuration
@ConditionalOnClass(FirebaseApp.class)
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebaseAutoConfiguration {

  private final FirebaseProperties properties;

  @Autowired
  public FirebaseAutoConfiguration(FirebaseProperties properties) {
    this.properties = properties;
  }

  private static FirebaseOptions getFirebaseOptions(FirebaseProperties properties)
      throws IOException {
    return new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setDatabaseUrl(properties.getDatabaseUrl())
        .build();
  }

  @ConditionalOnMissingBean
  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    FirebaseOptions options = getFirebaseOptions(properties);
    return FirebaseApp.initializeApp(options);
  }

  @ConditionalOnMissingBean
  @Lazy
  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
    return FirebaseMessaging.getInstance(app);
  }

  @ConditionalOnMissingBean
  @Bean
  public FirebaseAuth firebaseAuth(FirebaseApp app) {
    return FirebaseAuth.getInstance(app);
  }
}
