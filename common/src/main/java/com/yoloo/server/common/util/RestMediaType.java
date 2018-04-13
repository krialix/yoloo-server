package com.yoloo.server.common.util;

import org.springframework.http.MediaType;

import java.util.Map;

public class RestMediaType extends MediaType {

  public static final MediaType APPLICATION_PATCH_JSON;
  public static final MediaType APPLICATION_API_JSON_VND;

  public static final String APPLICATION_PATCH_JSON_VALUE = "application/json-patch+json";
  public static final String APPLICATION_API_JSON_VND_VALUE = "application/vnd.api+json";

  static {
    APPLICATION_PATCH_JSON = valueOf(APPLICATION_PATCH_JSON_VALUE);
    APPLICATION_API_JSON_VND = valueOf(APPLICATION_API_JSON_VND_VALUE);
  }

  public RestMediaType(MediaType type) {
    super(type, (Map<String, String>) null);
  }
}
