package com.yoloo.spring.autoconfiguration.id.config;

import com.yoloo.spring.autoconfiguration.id.generator.CachedSnowflakeIdGenerator;
import com.yoloo.spring.autoconfiguration.id.generator.IdFactory;
import com.yoloo.spring.autoconfiguration.id.generator.TimestampUUIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class IdGeneratorAutoConfiguration {

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
}
