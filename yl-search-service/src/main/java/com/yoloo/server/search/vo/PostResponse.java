package com.yoloo.server.search.vo;

import java.util.List;

public class PostResponse {

  private String id;
  private Title title;
  private Content content;
  private List<Tag> tags;

  private PostResponse() {}

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

  @Override
  public String toString() {
    return "PostResponse{"
        + "id='"
        + id
        + '\''
        + ", title="
        + title
        + ", content="
        + content
        + ", tags="
        + tags
        + '}';
  }

  public static class Title {

    private String value;

    private Title() {}

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Title{" + "value='" + value + '\'' + '}';
    }
  }

  public static class Content {

    private String value;

    private Content() {}

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Content{" + "value='" + value + '\'' + '}';
    }
  }

  public static class Tag {

    private String value;

    private Tag() {}

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Tag{" + "value='" + value + '\'' + '}';
    }
  }
}
