package com.yoloo.server.common.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.io.Files;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseAutoConfiguration {

  private final FirebaseYAMLConfig yamlConfig;

  public FirebaseAutoConfiguration(FirebaseYAMLConfig yamlConfig) {
    this.yamlConfig = yamlConfig;
  }

  @ConditionalOnMissingBean(FirebaseApp.class)
  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    File file =
        ResourceUtils.getFile(
            ResourceUtils.CLASSPATH_URL_PREFIX + yamlConfig.getServiceAccountKeyName());

    InputStream inputStream = Files.asByteSource(file).openStream();

    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(inputStream))
            .setDatabaseUrl(yamlConfig.getDatabaseUrl())
            .build();

    return FirebaseApp.initializeApp(options);
  }

  @ConditionalOnMissingBean(FirebaseMessaging.class)
  @Lazy
  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
    return FirebaseMessaging.getInstance(app);
  }

  @ConditionalOnMissingBean(FirebaseAuth.class)
  @Bean
  public FirebaseAuth firebaseAuth(FirebaseApp app) {
    return FirebaseAuth.getInstance(app);
  }
}
