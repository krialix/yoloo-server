package com.yoloo.server.common.util.id;

import com.yoloo.tools.impl.DefaultUidGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class SnowflakeIdGenerator implements LongIdGenerator {

  @Override
  public long generateId() {
    return DefaultUidGenerator.defaultUidGenerator().getUID();
  }
}
