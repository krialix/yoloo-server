package com.yoloo.server.common.util;

import com.google.appengine.api.utils.SystemProperty;
import com.google.apphosting.api.ApiProxy;

import java.util.Map;

public final class AppengineUtil {

  private AppengineUtil() {}

  public static String getAppengineRunningUrl() {
    final ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
    final Map<String, Object> attributes = env.getAttributes();
    final String hostAndPort =
        (String) attributes.get("com.google.appengine.runtime.default_version_hostname");
    return "http://" + hostAndPort;
  }

  public static boolean isTest() {
    return SystemProperty.environment.value() == null;
  }
}
