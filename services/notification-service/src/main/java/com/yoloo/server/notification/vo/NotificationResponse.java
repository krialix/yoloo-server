package com.yoloo.server.notification.vo;

public class NotificationResponse {
  private long id;
  private String type;
  private ActorResponse actor;
  private ReceiverResponse receiver;

  private NotificationResponse(Builder builder) {
    id = builder.id;
    type = builder.type;
    actor = builder.actor;
    receiver = builder.receiver;
  }

  private NotificationResponse() {}

  public static Builder newBuilder() {
    return new Builder();
  }

  public long getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public ActorResponse getActor() {
    return actor;
  }

  public ReceiverResponse getReceiver() {
    return receiver;
  }

  public static class ActorResponse {
    private long id;
    private String displayName;
    private String avatarUrl;

    private ActorResponse() {}

    private ActorResponse(Builder builder) {
      id = builder.id;
      displayName = builder.displayName;
      avatarUrl = builder.avatarUrl;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public long getId() {
      return id;
    }

    public String getDisplayName() {
      return displayName;
    }

    public String getAvatarUrl() {
      return avatarUrl;
    }

    public static final class Builder {
      private long id;
      private String displayName;
      private String avatarUrl;

      private Builder() {}

      public Builder id(long val) {
        id = val;
        return this;
      }

      public Builder displayName(String val) {
        displayName = val;
        return this;
      }

      public Builder avatarUrl(String val) {
        avatarUrl = val;
        return this;
      }

      public ActorResponse build() {
        return new ActorResponse(this);
      }
    }
  }

  public static class ReceiverResponse {
    private long id;

    private ReceiverResponse() {}

    private ReceiverResponse(Builder builder) {
      id = builder.id;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public long getId() {
      return id;
    }

    public static final class Builder {
      private long id;

      private Builder() {}

      public Builder id(long val) {
        id = val;
        return this;
      }

      public ReceiverResponse build() {
        return new ReceiverResponse(this);
      }
    }
  }

  public static final class Builder {
    private long id;
    private String type;
    private ActorResponse actor;
    private ReceiverResponse receiver;

    private Builder() {}

    public Builder id(long val) {
      id = val;
      return this;
    }

    public Builder type(String val) {
      type = val;
      return this;
    }

    public Builder actor(ActorResponse val) {
      actor = val;
      return this;
    }

    public Builder receiver(ReceiverResponse val) {
      receiver = val;
      return this;
    }

    public NotificationResponse build() {
      return new NotificationResponse(this);
    }
  }
}
