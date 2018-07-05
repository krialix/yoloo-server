package com.yoloo.server.common.vo.response;

public class MediaResponse {
  private String type;
  private String mediaUrl;

  private MediaResponse() {}

  private MediaResponse(String type, String mediaUrl) {
    this.type = type;
    this.mediaUrl = mediaUrl;
  }

  public static MediaResponse create(String type, String mediaUrl) {
    return new MediaResponse(type, mediaUrl);
  }

  public String getType() {
    return type;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }
}
