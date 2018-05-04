package com.yoloo.server.common.id.generator;

import com.yoloo.tools.impl.CachedUidGenerator;
import org.springframework.stereotype.Component;

@Component("cached")
public class CachedSnowflakeIdGenerator implements LongIdGenerator {

  @Override
  public long generateId() {
    return CachedUidGenerator.defaultUidGenerator().getUID();
  }
}
