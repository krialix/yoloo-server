package com.yoloo.server.common.vo.response;

public class ResponsePagination {
  private String next;
  private String prev;

  private ResponsePagination() {}

  private ResponsePagination(Builder builder) {
    next = builder.next;
    prev = builder.prev;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public String getNext() {
    return next;
  }

  public String getPrev() {
    return prev;
  }

  public static final class Builder {
    private String next;
    private String prev;

    private Builder() {}

    public Builder next(String val) {
      next = val;
      return this;
    }

    public Builder prev(String val) {
      prev = val;
      return this;
    }

    public ResponsePagination build() {
      return new ResponsePagination(this);
    }
  }
}
