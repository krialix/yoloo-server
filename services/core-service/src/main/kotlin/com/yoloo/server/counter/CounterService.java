package com.yoloo.server.counter;

/**
 * A service that can be used to increment, decrement and reset a named counter value.
 *
 * @author Dave Syer
 */
public interface CounterService {

  /**
   * Increment the specified counter by 1.
   * @param metricName the name of the counter
   */
  void increment(String metricName);

  /**
   * Decrement the specified counter by 1.
   * @param metricName the name of the counter
   */
  void decrement(String metricName);

  /**
   * Reset the specified counter.
   * @param metricName the name of the counter
   */
  void reset(String metricName);

}
