package com.yoloo.server.comment.vo;

import java.util.Collection;

public class CommentCollectionResponse {

  private CommentResponse approvedComment;
  private String nextPageToken;
  private String prevPageToken;
  private Collection<CommentResponse> data;

  private CommentCollectionResponse() {}

  private CommentCollectionResponse(Builder builder) {
    this.approvedComment = builder.approvedComment;
    this.nextPageToken = builder.nextPageToken;
    this.prevPageToken = builder.prevPageToken;
    this.data = builder.data;
  }

  public static Builder builder() {
    return new Builder();
  }

  public CommentResponse getApprovedComment() {
    return approvedComment;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public String getPrevPageToken() {
    return prevPageToken;
  }

  public Collection<CommentResponse> getData() {
    return data;
  }

  @Override
  public String toString() {
    return "CommentCollectionResponse{"
        + "approvedComment="
        + approvedComment
        + ", nextPageToken='"
        + nextPageToken
        + '\''
        + ", prevPageToken='"
        + prevPageToken
        + '\''
        + ", data="
        + data
        + '}';
  }

  public static class Builder {

    private CommentResponse approvedComment;
    private String nextPageToken;
    private String prevPageToken;
    private Collection<CommentResponse> data;

    private Builder() {}

    public Builder approvedComment(CommentResponse approvedComment) {
      this.approvedComment = approvedComment;
      return this;
    }

    public Builder nextPageToken(String nextPageToken) {
      this.nextPageToken = nextPageToken;
      return this;
    }

    public Builder prevPageToken(String prevPageToken) {
      this.prevPageToken = prevPageToken;
      return this;
    }

    public Builder data(Collection<CommentResponse> data) {
      this.data = data;
      return this;
    }

    public CommentCollectionResponse build() {
      return new CommentCollectionResponse(this);
    }
  }
}
