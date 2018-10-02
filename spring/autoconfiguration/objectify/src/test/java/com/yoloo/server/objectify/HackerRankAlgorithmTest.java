package com.yoloo.server.objectify;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

public class HackerRankAlgorithmTest {
  private static final DecimalFormat FORMAT = new DecimalFormat("#0.000000");

  private static double score(Instant createdAt, int likes, boolean approved) {
    long age = Duration.between(Instant.now(), createdAt).abs().toHours();
    System.out.print("Age: " + age + " - ");
    return Math.abs(likes - 1) * (approved ? Integer.MAX_VALUE : 1) / (Math.pow(age + 2, 1.8));
  }

  private static void print(double value) {
    System.out.println(FORMAT.format(value));
  }

  @Test
  public void testScore1() {
    print(score(Instant.now(), 0, true));
    print(score(Instant.now(), 0, false));
    print(score(Instant.now(), 10000, false));

    print(score(Instant.now().minus(Duration.ofDays(1)), 100, false));
    print(score(Instant.now().minus(Duration.ofDays(365)), 100, false));
    print(score(Instant.now().minus(Duration.ofDays(100)), 1, false));
    print(score(Instant.now().minus(Duration.ofDays(100)), 0, false));
  }
}
