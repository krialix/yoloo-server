package com.yoloo.server.notification.vo;

import java.util.Collection;

public class NotificationCollectionResponse {
  private String nextPageToken;
  private String prevPageToken;
  private Collection<NotificationResponse> data;

  private NotificationCollectionResponse() {}

  private NotificationCollectionResponse(Builder builder) {
    this.nextPageToken = builder.nextPageToken;
    this.prevPageToken = builder.prevPageToken;
    this.data = builder.data;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public String getPrevPageToken() {
    return prevPageToken;
  }

  public Collection<NotificationResponse> getData() {
    return data;
  }

  public static class Builder {
    private String nextPageToken;
    private String prevPageToken;
    private Collection<NotificationResponse> data;

    private Builder() {}

    public Builder nextPageToken(String nextPageToken) {
      this.nextPageToken = nextPageToken;
      return this;
    }

    public Builder prevPageToken(String prevPageToken) {
      this.prevPageToken = prevPageToken;
      return this;
    }

    public Builder data(Collection<NotificationResponse> data) {
      this.data = data;
      return this;
    }

    public NotificationCollectionResponse build() {
      return new NotificationCollectionResponse(this);
    }
  }
}
