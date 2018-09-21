package com.yoloo.spring.autoconfiguration.id.generator;

import com.yoloo.tools.impl.CachedUidGenerator;

public class CachedSnowflakeIdGenerator implements IdFactory.LongIdGenerator {

  @Override
  public long generateId() {
    return CachedUidGenerator.defaultUidGenerator().getUID();
  }
}
