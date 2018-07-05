package com.yoloo.server.common.vo.response;

public class ResponseData {
  private String type;
  private PostResponseData data;

  private ResponseData() {}

  private ResponseData(String type, PostResponseData data) {
    this.type = type;
    this.data = data;
  }

  public static ResponseData create(String type, PostResponseData data) {
    return new ResponseData(type, data);
  }

  public String getType() {
    return type;
  }

  public PostResponseData getData() {
    return data;
  }
}
