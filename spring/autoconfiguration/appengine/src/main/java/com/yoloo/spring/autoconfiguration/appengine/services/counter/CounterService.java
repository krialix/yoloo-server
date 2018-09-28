package com.yoloo.spring.autoconfiguration.appengine.services.counter;

/**
 * A service that can be used to increment, decrement and reset a named counter value.
 *
 * @author Dave Syer
 */
public interface CounterService {

  /**
   * Increment the specified counter by 1.
   *
   * @param metricNames the name of the counter
   */
  void increment(String... metricNames);

  /**
   * Decrement the specified counter by 1.
   *
   * @param metricNames the name of the counter
   */
  void decrement(String... metricNames);

  /**
   * Reset the specified counter.
   *
   * @param metricNames the name of the counter
   */
  void reset(String... metricNames);
}
