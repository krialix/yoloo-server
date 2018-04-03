package com.yoloo.server.common.jsonapi;

import java.util.List;

public class JsonApiResponse<T> {
  private JsonApiData<T> data;
  private List<JsonApiError> errors;
  private JsonApiMeta meta;

  public JsonApiResponse(JsonApiData<T> data) {
    this.data = data;
  }
}
