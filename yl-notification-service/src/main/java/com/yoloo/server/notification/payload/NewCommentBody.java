package com.yoloo.server.notification.payload;

public class NewCommentBody implements NotificationBody {
  private String token;
  private String postId;
  private String trimmedCommentContent;

  private NewCommentBody() {}

  public String getToken() {
    return token;
  }

  public String getPostId() {
    return postId;
  }

  public String getTrimmedCommentContent() {
    return trimmedCommentContent;
  }

  @Override
  public String toString() {
    return "NewComment{"
        + "token='"
        + token
        + '\''
        + ", postId='"
        + postId
        + '\''
        + ", trimmedCommentContent='"
        + trimmedCommentContent
        + '\''
        + '}';
  }
}
