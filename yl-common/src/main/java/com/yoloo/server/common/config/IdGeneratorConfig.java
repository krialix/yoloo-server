package com.yoloo.server.common.config;

import com.yoloo.server.common.id.CachedSnowflakeIdGenerator;
import com.yoloo.server.common.id.InstagramIdGenerator;
import com.yoloo.server.common.id.LongIdGenerator;
import com.yoloo.server.common.id.SnowflakeIdGenerator;
import com.yoloo.server.common.id.StringIdGenerator;
import com.yoloo.server.common.id.TimestampUUIDGenerator;
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
