package com.yoloo.server.notification.payload;

public class NewPostBody implements NotificationBody {
  private String topic;
  private String postId;
  private String postTitle;
  private String bounty;
  private String groupId;

  private NewPostBody() {}

  public String getTopic() {
    return topic;
  }

  public String getPostId() {
    return postId;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public String getBounty() {
    return bounty;
  }

  public String getGroupId() {
    return groupId;
  }

  @Override
  public String toString() {
    return "NewPost{"
        + "topic='"
        + topic
        + '\''
        + ", postId='"
        + postId
        + '\''
        + ", postTitle='"
        + postTitle
        + '\''
        + ", bounty='"
        + bounty
        + '\''
        + ", groupId='"
        + groupId
        + '\''
        + '}';
  }
}
