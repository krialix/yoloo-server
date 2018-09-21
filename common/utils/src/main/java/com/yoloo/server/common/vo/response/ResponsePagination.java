package com.yoloo.server.common.vo.response;

public class ResponsePagination {
  private String prev;
  private String next;

  private ResponsePagination() {}

  private ResponsePagination(String prev, String next) {
    this.next = next;
    this.prev = prev;
  }

  public static ResponsePagination create(String prev, String next) {
    return new ResponsePagination(prev, next);
  }

  public String getNext() {
    return next;
  }

  public String getPrev() {
    return prev;
  }
}
