package com.yoloo.server.notification.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Notification {

  @Id private long id;

  private Actor actor;

  private Receiver receiver;

  private EntityType type;

  private Notification() {}

  private Notification(Builder builder) {
    id = builder.id;
    actor = builder.actor;
    receiver = builder.receiver;
    type = builder.type;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public long getId() {
    return id;
  }

  public Actor getActor() {
    return actor;
  }

  public Receiver getReceiver() {
    return receiver;
  }

  public EntityType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Notification{"
        + "id="
        + id
        + ", actor="
        + actor
        + ", receiver="
        + receiver
        + ", type="
        + type
        + '}';
  }

  public enum EntityType {
    FOLLOWED(Entity.RELATIONSHIP),
    COMMENTED(Entity.POST),
    COMMENT_APPROVED(Entity.POST),
    MENTIONED(Entity.POST),
    EARNED_POINTS(Entity.GAMIFICATION);

    private final Entity parent;

    EntityType(Entity parent) {
      this.parent = parent;
    }

    enum Entity {
      GAMIFICATION,
      RELATIONSHIP,
      POST
    }
  }

  public static class Actor {
    private long id;
    private String displayName;
    private String avatarUrl;

    private Actor() {}

    private Actor(Builder builder) {
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

    @Override
    public String toString() {
      return "Actor{"
          + "id="
          + id
          + ", displayName='"
          + displayName
          + '\''
          + ", avatarUrl='"
          + avatarUrl
          + '\''
          + '}';
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

      public Actor build() {
        return new Actor(this);
      }
    }
  }

  public static class Receiver {
    @Index private long id;

    private Receiver() {}

    private Receiver(Builder builder) {
      id = builder.id;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public long getId() {
      return id;
    }

    @Override
    public String toString() {
      return "Receiver{" + "id=" + id + '}';
    }

    public static final class Builder {
      private long id;

      private Builder() {}

      public Builder id(long val) {
        id = val;
        return this;
      }

      public Receiver build() {
        return new Receiver(this);
      }
    }
  }

  public static final class Builder {
    private long id;
    private Actor actor;
    private Receiver receiver;
    private EntityType type;

    private Builder() {}

    public Builder id(long val) {
      id = val;
      return this;
    }

    public Builder actor(Actor val) {
      actor = val;
      return this;
    }

    public Builder receiver(Receiver val) {
      receiver = val;
      return this;
    }

    public Builder type(EntityType val) {
      type = val;
      return this;
    }

    public Notification build() {
      return new Notification(this);
    }
  }
}
