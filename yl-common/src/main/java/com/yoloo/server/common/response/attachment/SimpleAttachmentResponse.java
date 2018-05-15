package com.yoloo.server.common.response.attachment;

public class SimpleAttachmentResponse implements AttachmentResponse {
  private final String url;

  private SimpleAttachmentResponse() {
    this(null);
  }

  public SimpleAttachmentResponse(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
