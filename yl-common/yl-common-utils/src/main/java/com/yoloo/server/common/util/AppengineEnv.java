package com.yoloo.server.common.util;

import com.google.appengine.api.utils.SystemProperty;

public enum AppengineEnv {
  PROD,
  DEV,
  TEST;

  public static AppengineEnv currentEnv() {
    switch (SystemProperty.environment.value()) {
      case Production:
        return AppengineEnv.PROD;
      case Development:
        return AppengineEnv.DEV;
      default:
        return AppengineEnv.TEST;
    }
  }

  public static boolean isTest() {
    return currentEnv() == AppengineEnv.TEST;
  }

  public static boolean isProd() {
    return currentEnv() == AppengineEnv.PROD;
  }

  public static boolean isDev() {
    return currentEnv() == AppengineEnv.DEV;
  }
}
