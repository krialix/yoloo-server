package com.yoloo.server.common.util;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

import static com.google.common.io.Resources.asByteSource;
import static com.google.common.io.Resources.getResource;
import static java.nio.charset.StandardCharsets.UTF_8;

/** Utility methods related to reading java resources. */
public final class ResourceUtils {

  /** Loads a resource from a file as a string, assuming UTF-8 encoding. */
  public static String readResourceUtf8(String filename) {
    return readResourceUtf8(getResource(filename));
  }

  /**
   * Loads a resource from a file (specified relative to the contextClass) as a string, assuming
   * UTF-8 encoding.
   */
  public static String readResourceUtf8(Class<?> contextClass, String filename) {
    return readResourceUtf8(getResource(contextClass, filename));
  }

  /** Loads a resource from a URL as a string, assuming UTF-8 encoding. */
  public static String readResourceUtf8(URL url) {
    try {
      return Resources.toString(url, UTF_8);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to load resource: " + url, e);
    }
  }

  /** Loads a file (specified relative to the contextClass) as a ByteSource. */
  public static ByteSource readResourceBytes(Class<?> contextClass, String filename) {
    return asByteSource(getResource(contextClass, filename));
  }
}
