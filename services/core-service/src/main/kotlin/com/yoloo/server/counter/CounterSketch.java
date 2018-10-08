package com.yoloo.server.counter;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.yoloo.server.entity.Keyable;
import com.yoloo.server.util.Pair;
import com.yoloo.server.util.sketch.CountMinSketch;

@Cache
@Entity
class CounterSketch implements Keyable<CounterSketch> {
  private static final String ENTITY_ID = "counter:sketch";

  @Id private String id;
  private CountMinSketch incrementSketch;
  private CountMinSketch decrementSketch;

  private CounterSketch() {
    this.id = ENTITY_ID;
    this.incrementSketch = new CountMinSketch();
    this.decrementSketch = new CountMinSketch();
  }

  private CounterSketch(CountMinSketch incrementSketch, CountMinSketch decrementSketch) {
    this.id = ENTITY_ID;
    this.incrementSketch = incrementSketch;
    this.decrementSketch = decrementSketch;
  }

  public static CounterSketch create() {
    return new CounterSketch();
  }

  static Key<CounterSketch> createKey() {
    return Key.create(CounterSketch.class, ENTITY_ID);
  }

  String getId() {
    return id;
  }

  CountMinSketch getIncrementSketch() {
    return incrementSketch;
  }

  CountMinSketch getDecrementSketch() {
    return decrementSketch;
  }

  CounterSketch merge(CounterSketch other) {
    incrementSketch.merge(other.incrementSketch);
    decrementSketch.merge(other.decrementSketch);
    return new CounterSketch(incrementSketch, decrementSketch);
  }

  private int getEstimatedCount(String key) {
    return incrementSketch.getEstimatedCount(key) - decrementSketch.getEstimatedCount(key);
  }

  Pair<String, Integer> getEstimatedCountPair(String key) {
    return Pair.of(key, getEstimatedCount(key));
  }
}
