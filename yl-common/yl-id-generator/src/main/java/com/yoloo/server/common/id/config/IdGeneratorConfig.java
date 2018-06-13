package com.yoloo.server.common.id.config;

import com.yoloo.server.common.id.generator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class IdGeneratorConfig {

  @Lazy
  @Bean("timestamp")
  public StringIdGenerator timestampUUIDGenerator() {
    return new TimestampUUIDGenerator();
  }

  @Lazy
  @Bean("cached")
  public LongIdGenerator cachedSnowflakeIdGenerator() {
    return new CachedSnowflakeIdGenerator();
  }

  @Lazy
  @Primary
  @Bean
  public LongIdGenerator snowflakeIdGenerator() {
    return new SnowflakeIdGenerator();
  }

  @Lazy
  @Bean("instagram")
  public LongIdGenerator instagramIdGenerator() {
    return new InstagramIdGenerator();
  }
}
