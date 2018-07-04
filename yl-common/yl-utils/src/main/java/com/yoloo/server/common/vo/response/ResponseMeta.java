package com.yoloo.server.common.vo.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.HashMap;
import java.util.Map;

public class ResponseMeta {
  private ResponsePagination pagination;
  @JsonUnwrapped private Map<String, Object> extras;

  private ResponseMeta(Builder builder) {
    pagination = builder.pagination;
    extras = builder.extras;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public ResponsePagination getPagination() {
    return pagination;
  }

  public Map<String, Object> getExtras() {
    return extras;
  }

  public static final class Builder {
    private ResponsePagination pagination;
    private Map<String, Object> extras;

    private Builder() {
      this.extras = new HashMap<>();
    }

    public Builder pagination(ResponsePagination val) {
      pagination = val;
      return this;
    }

    public Builder extra(String key, Object value) {
      extras.putIfAbsent(key, value);
      return this;
    }

    public ResponseMeta build() {
      return new ResponseMeta(this);
    }
  }
}
