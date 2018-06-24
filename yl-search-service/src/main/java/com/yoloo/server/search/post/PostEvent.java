package com.yoloo.server.search.post;

import java.util.List;

public class PostEvent {
  private String type;
  private Payload payload;

  private PostEvent() {}

  public String getType() {
    return type;
  }

  public Payload getPayload() {
    return payload;
  }

  static class Payload {
    private String id;
    private Title title;
    private Content content;
    private List<Tag> tags;

    private Payload() {
    }

    public String getId() {
      return id;
    }

    public Title getTitle() {
      return title;
    }

    public Content getContent() {
      return content;
    }

    public List<Tag> getTags() {
      return tags;
    }
  }

  static class Title {
    String value;

    private Title() {}

    public String getValue() {
      return value;
    }
  }

  static class Content {
    String value;

    private Content() {}

    public String getValue() {
      return value;
    }
  }

  static class Tag {
    String value;

    private Tag() {}

    public String getValue() {
      return value;
    }
  }

  static class BuddyRequest {}
}
