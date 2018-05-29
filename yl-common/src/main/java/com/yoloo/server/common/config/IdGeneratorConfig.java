package com.yoloo.server.common.config;

import com.yoloo.server.common.util.id.CachedSnowflakeIdGenerator;
import com.yoloo.server.common.util.id.InstagramIdGenerator;
import com.yoloo.server.common.util.id.LongIdGenerator;
import com.yoloo.server.common.util.id.SnowflakeIdGenerator;
import com.yoloo.server.common.util.id.StringIdGenerator;
import com.yoloo.server.common.util.id.TimestampUUIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class IdGeneratorConfig {

  @Bean("timestamp")
  public StringIdGenerator timestampUUIDGenerator() {
    return new TimestampUUIDGenerator();
  }

  @Bean("cached")
  public LongIdGenerator cachedSnowflakeIdGenerator() {
    return new CachedSnowflakeIdGenerator();
  }

  @Primary
  @Bean
  public LongIdGenerator snowflakeIdGenerator() {
    return new SnowflakeIdGenerator();
  }

  @Bean("instagram")
  public LongIdGenerator instagramIdGenerator() {
    return new InstagramIdGenerator();
  }
}
