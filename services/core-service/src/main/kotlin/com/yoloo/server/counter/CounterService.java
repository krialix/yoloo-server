package com.yoloo.server.counter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * A service that can be used to increment, decrement and reset a named counter value.
 *
 * @author Dave Syer
 */
public interface CounterService {

  Map<String, Integer> getCounts(Collection<String> keys);

  /**
   * Increment the specified counter by 1.
   *
   * @param metricNames the name of the counter
   */
  default void increment(String... metricNames) {
    increment(Arrays.asList(metricNames));
  }

  void increment(Collection<String> metricNames);

  /**
   * Decrement the specified counter by 1.
   *
   * @param metricNames the name of the counter
   */
  default void decrement(String... metricNames) {
    decrement(Arrays.asList(metricNames));
  }

  void decrement(Collection<String> metricNames);
}
