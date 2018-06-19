package com.yoloo.server.common.id.config;

import com.yoloo.server.common.id.generator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class IdGeneratorAutoConfiguration {

  @Lazy
  @Bean(IdGenQualifier.TIMESTAMP)
  public StringIdGenerator timestampUUIDGenerator() {
    return new TimestampUUIDGenerator();
  }

  @Lazy
  @Bean(IdGenQualifier.CACHED)
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
  @Bean(IdGenQualifier.INSTAGRAM)
  public LongIdGenerator instagramIdGenerator() {
    return new InstagramIdGenerator();
  }
}
