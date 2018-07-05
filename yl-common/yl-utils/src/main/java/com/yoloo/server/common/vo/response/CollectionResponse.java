package com.yoloo.server.common.vo.response;

import java.util.List;

public class CollectionResponse {
  private ResponseMeta meta;
  private List<ResponseData> data;

  private CollectionResponse() {}

  private CollectionResponse(ResponseMeta meta, List<ResponseData> data) {
    this.meta = meta;
    this.data = data;
  }

  public static CollectionResponse create(ResponseMeta meta, List<ResponseData> data) {
    return new CollectionResponse(meta, data);
  }

  public ResponseMeta getMeta() {
    return meta;
  }

  public List<ResponseData> getData() {
    return data;
  }
}
