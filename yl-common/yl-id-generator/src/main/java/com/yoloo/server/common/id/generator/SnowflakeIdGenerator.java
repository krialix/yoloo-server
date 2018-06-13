package com.yoloo.server.common.id.generator;

import com.yoloo.tools.impl.DefaultUidGenerator;

public class SnowflakeIdGenerator implements LongIdGenerator {

  @Override
  public long generateId() {
    return DefaultUidGenerator.defaultUidGenerator().getUID();
  }
}
