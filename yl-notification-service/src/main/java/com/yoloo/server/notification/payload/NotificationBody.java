package com.yoloo.server.notification.payload;

public interface NotificationBody {

  class Follow implements NotificationBody {
    private String token;
    private String followerId;
    private String followingId;
    private String followerName;
    private String followerImage;

    private Follow() {}

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

  class NewPost implements NotificationBody {
    private String topic;
    private String postId;
    private String postTitle;
    private String bounty;
    private String groupId;

    private NewPost() {}

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

  class NewComment implements NotificationBody {
    private String token;
    private String postId;
    private String trimmedContent;

    private NewComment() {}

    public String getToken() {
      return token;
    }

    public String getPostId() {
      return postId;
    }

    public String getTrimmedContent() {
      return trimmedContent;
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
          + ", trimmedContent='"
          + trimmedContent
          + '\''
          + '}';
    }
  }

  class Approve implements NotificationBody {
    private String token;
    private String postId;
    private String postOwnerId;
    private String commentOwnerId;
    private String trimmedCommentContent;

    private Approve() {}

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
}
