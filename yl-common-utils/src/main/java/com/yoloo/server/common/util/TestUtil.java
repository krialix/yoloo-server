package com.yoloo.server.common.util;

import com.googlecode.objectify.Result;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface TestUtil {

  static void saveResultsNowIfTest(Result<?>... results) {
    if (AppengineEnv.isTest()) {
      Arrays.asList(results).forEach(Result::now);
    }
  }

  static void saveFuturesNowIfTest(Future<?>... futures) {
    if (AppengineEnv.isTest()) {
      Arrays.asList(futures).forEach(future -> {
        try {
          future.get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      });
    }
  }
}
