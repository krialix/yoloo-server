package com.yoloo.server.notification.payload;

public interface NotificationBody {

  class Follow implements NotificationBody {
    private String token;
    private String followerId;
    private String followerName;
    private String followerImage;

    public String getToken() {
      return token;
    }

    public String getFollowerId() {
      return followerId;
    }

    public String getFollowerName() {
      return followerName;
    }

    public String getFollowerImage() {
      return followerImage;
    }
  }

  class NewPost implements NotificationBody {
    private String topic;
    private String postId;
    private String postTitle;
    private String bounty;
    private String groupId;

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
  }

  class NewComment implements NotificationBody {
    private String token;
    private String postId;
    private String trimmedContent;

    public String getToken() {
      return token;
    }

    public String getPostId() {
      return postId;
    }

    public String getTrimmedContent() {
      return trimmedContent;
    }
  }

  class Approve implements NotificationBody {
    private String token;
    private String postId;
    private String trimmedContent;

    public String getToken() {
      return token;
    }

    public String getPostId() {
      return postId;
    }

    public String getTrimmedContent() {
      return trimmedContent;
    }
  }
}
