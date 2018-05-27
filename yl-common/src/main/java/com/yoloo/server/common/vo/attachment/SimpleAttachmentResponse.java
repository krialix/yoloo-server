package com.yoloo.server.common.vo.attachment;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleAttachmentResponse that = (SimpleAttachmentResponse) o;
    return Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }

  @Override
  public String toString() {
    return "SimpleAttachmentResponse{" + "url='" + url + '\'' + '}';
  }
}
