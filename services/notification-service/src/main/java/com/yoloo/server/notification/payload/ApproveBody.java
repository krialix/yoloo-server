package com.yoloo.server.notification.payload;

public class ApproveBody implements NotificationBody {
  private String token;
  private String postId;
  private String postOwnerId;
  private String commentOwnerId;
  private String trimmedCommentContent;

  private ApproveBody() {}

  public String getToken() {
    return token;
  }

  public String getPostId() {
    return postId;
  }

  public String getPostOwnerId() {
    return postOwnerId;
  }

  public String getCommentOwnerId() {
    return commentOwnerId;
  }

  public String getTrimmedCommentContent() {
    return trimmedCommentContent;
  }

  @Override
  public String toString() {
    return "Approve{"
        + "token='"
        + token
        + '\''
        + ", postId='"
        + postId
        + '\''
        + ", postOwnerId='"
        + postOwnerId
        + '\''
        + ", commentOwnerId='"
        + commentOwnerId
        + '\''
        + ", trimmedCommentContent='"
        + trimmedCommentContent
        + '\''
        + '}';
  }
}
