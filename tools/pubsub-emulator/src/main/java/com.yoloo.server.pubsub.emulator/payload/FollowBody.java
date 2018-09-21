package com.yoloo.server.pubsub.emulator.payload;

public class FollowBody implements NotificationBody {
  private String token;
  private String followerId;
  private String followingId;
  private String followerName;
  private String followerImage;

  private FollowBody() {}

  public String getToken() {
    return token;
  }

  public String getFollowerId() {
    return followerId;
  }

  public String getFollowingId() {
    return followingId;
  }

  public String getFollowerName() {
    return followerName;
  }

  public String getFollowerImage() {
    return followerImage;
  }

  @Override
  public String toString() {
    return "Follow{"
        + "token='"
        + token
        + '\''
        + ", followerId='"
        + followerId
        + '\''
        + ", followingId='"
        + followingId
        + '\''
        + ", followerName='"
        + followerName
        + '\''
        + ", followerImage='"
        + followerImage
        + '\''
        + '}';
  }
}
