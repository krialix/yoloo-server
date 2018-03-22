package com.yoloo.server.core.objectify.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.function.Supplier;

public class ObjectifyUtil {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectifyUtil.class);

  public static <T> T measureAction(Supplier<T> action) {
    String name = "scan entity classes";
    StopWatch stopWatch = new StopWatch();
    if (LOG.isDebugEnabled()) {
      stopWatch.start(name);
    }
    T result = action.get();
    if (LOG.isDebugEnabled()) {
      stopWatch.stop();
      LOG.debug("{} took {} milliseconds", name, stopWatch.getTotalTimeMillis());
    }

    return result;
  }
}
