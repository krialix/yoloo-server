package com.yoloo.server.counter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.yoloo.server.queue.QueueNames;
import com.yoloo.server.queue.QueuePayload;
import com.yoloo.server.util.Pair;
import com.yoloo.server.util.sketch.CountMinSketch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static java.util.stream.Collectors.toMap;

@Service
public class CounterServiceImpl implements CounterService {

  private final Queue counterQueue;
  private final MemcacheService memcacheService;
  private final ObjectMapper objectMapper;

  @Autowired
  public CounterServiceImpl(
      @Qualifier(QueueNames.BATCH_SAVE_PULL_QUEUE) Queue batchSaveQueue,
      MemcacheService memcacheService,
      ObjectMapper objectMapper) {
    this.counterQueue = batchSaveQueue;
    this.memcacheService = memcacheService;
    this.objectMapper = objectMapper;
  }

  @Override
  public Map<String, Integer> getCounts(Collection<String> keys) {
    CounterSketch sketch = ofy().load().key(CounterSketch.createKey()).now();

    return keys.stream()
        .map(sketch::getEstimatedCountPair)
        .collect(toMap(Pair::getFirst, Pair::getSecond));
  }

  @Override
  public void increment(Collection<String> metricNames) {
    updateCounter(metricNames, CounterSketch::getIncrementSketch);
  }

  @Override
  public void decrement(Collection<String> metricNames) {
    updateCounter(metricNames, CounterSketch::getDecrementSketch);
  }

  private void updateCounter(
      Collection<String> metricNames, Function<CounterSketch, CountMinSketch> map) {
    Key<CounterSketch> key = CounterSketch.createKey();
    CounterSketch counterSketch = ofy().load().key(key).now();
    CountMinSketch sketch = map.apply(counterSketch);
    metricNames.forEach(sketch::add);
    memcacheService.put(key.getRaw(), counterSketch);
    addToQueue(counterSketch);
  }

  private void addToQueue(CounterSketch counterSketch) {
    String json = sneak(() -> objectMapper.writeValueAsString(QueuePayload.save(counterSketch)));
    counterQueue.addAsync(TaskOptions.Builder.withTag("counter").payload(json));
  }
}
