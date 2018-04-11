package com.yoloo.server.common;

import com.google.api.core.ApiFuture;

import java.util.concurrent.ExecutionException;

public interface FcmNotification {

  ApiFuture<String> sendAsync();

  default String send() throws ExecutionException, InterruptedException {
   return sendAsync().get();
  }
}
