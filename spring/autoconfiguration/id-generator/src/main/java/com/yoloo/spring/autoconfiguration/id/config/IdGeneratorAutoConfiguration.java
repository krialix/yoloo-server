package com.yoloo.spring.autoconfiguration.id.config;

import com.arcticicestudio.icecore.hashids.Hashids;
import com.arcticicestudio.icecore.hashids.HashidsFeature;
import com.yoloo.spring.autoconfiguration.id.generator.CachedSnowflakeIdGenerator;
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory;
import com.yoloo.spring.autoconfiguration.id.generator.TimestampUUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
public class IdGeneratorAutoConfiguration {

  private final HashIdsProperties properties;

  @Autowired
  public IdGeneratorAutoConfiguration(HashIdsProperties properties) {
    this.properties = properties;
  }

  @Lazy
  @Bean(IdBeanQualifier.TIMESTAMP)
  public IdFactory.StringIdGenerator timestampUUIDGenerator() {
    return new TimestampUUIDGenerator();
  }

  @Lazy
  @Primary
  @Bean
  public IdFactory.LongIdGenerator snowflakeIdGenerator() {
    return new CachedSnowflakeIdGenerator();
  }

  @Lazy
  @Bean
  public Hashids defaultHashIds() {
    return new Hashids.Builder()
        .alphabet(properties.getAlphabet())
        .salt(properties.getSalt())
        .minLength(properties.getMinLength())
        .features(HashidsFeature.EXCEPTION_HANDLING)
        .build();
  }
}
