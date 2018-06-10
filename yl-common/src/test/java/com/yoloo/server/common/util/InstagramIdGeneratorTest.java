package com.yoloo.server.common.util;

import static java.time.Instant.EPOCH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.yoloo.server.common.id.IdGenerator;
import com.yoloo.server.common.id.InstagramIdGenerator;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by lcsontos on 2/20/17.
 */
public class InstagramIdGeneratorTest {

  private static final long TIME_DIFF = 10000;
  private static final Instant INSTANT = EPOCH.plusMillis(TIME_DIFF);
  private static final byte SHARD_ID = 126;

  @Test
  @Ignore
  public void testCollision() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(100);

    final int NUM_IDS = 1_000_000;
    final ConcurrentMap<Long, AtomicInteger> collisionMap = new ConcurrentHashMap<>(NUM_IDS);
    final IdGenerator idGenerator = new InstagramIdGenerator();

    for (int i = 0; i < NUM_IDS; i++) {
      executorService.submit(
          () -> {
            long id = ((InstagramIdGenerator) idGenerator).generateId();

            AtomicInteger collisionCount = collisionMap.get(id);
            if (collisionCount == null) {
              collisionCount = new AtomicInteger(0);
              AtomicInteger oldCollisionCount = collisionMap.putIfAbsent(id, collisionCount);
              if (oldCollisionCount != null) {
                collisionCount.addAndGet(oldCollisionCount.get());
              }
            } else {
              int currentCollisionCount = collisionCount.incrementAndGet();
              System.out.println(
                  String.format("%d collision(s) on ID %d", currentCollisionCount, id));
            }
          });
    }

    executorService.shutdown();
    executorService.awaitTermination(30, TimeUnit.SECONDS);

    double uniquePercent = collisionMap.size() * 100.0 / NUM_IDS;
    System.out.println(String.format("%f%% of all IDs are unique.", uniquePercent));

    assertTrue(uniquePercent > 99.9);
  }

  @Test
  public void testDoGenerate_withSerial() {
    long id = InstagramIdGenerator.doGenerate((byte) 0, 0L, 1);
    assertEquals(1L, id);
  }

  @Test
  public void testDoGenerate_withShardId() {
    long id = InstagramIdGenerator.doGenerate((byte) 1, 0L, 0);
    assertEquals(1L << 56, id);
  }

  @Test
  public void testDoGenerate_withTime() {
    long id = InstagramIdGenerator.doGenerate((byte) 0, 1L, 0);
    assertEquals(1L << 16, id);
  }

  @Test
  public void testExtractShardId() {
    long id = InstagramIdGenerator.doGenerate(SHARD_ID, TIME_DIFF, 10);
    byte shardId = InstagramIdGenerator.extractShardId(id);
    assertEquals(SHARD_ID, shardId);
  }

  @Test
  public void testExtractInstant() {
    long id = InstagramIdGenerator.doGenerate(SHARD_ID, TIME_DIFF, 10);
    Instant instant = InstagramIdGenerator.extractInstant(id);
    assertEquals(INSTANT.getEpochSecond(), instant.getEpochSecond());
  }

  @Test
  public void testGenerate() {
    IdGenerator idGenerator = new InstagramIdGenerator();
    long id = ((InstagramIdGenerator) idGenerator).generateId();
    assertNotEquals(0, id);
    System.out.println(id);
  }
}
