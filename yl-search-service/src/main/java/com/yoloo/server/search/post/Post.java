package com.yoloo.server.search.post;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;
import java.util.Objects;

@SolrDocument(collection = "post")
public class Post {

  @Id private String id;

  @Field private String title;

  @Field private String content;

  @Field private List<String> tags;

  private Post() {}

  private Post(Builder builder) {
    id = builder.id;
    title = builder.title;
    content = builder.content;
    tags = builder.tags;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return newBuilder().id(id).title(title).content(content).tags(tags);
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public List<String> getTags() {
    return tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Post post = (Post) o;
    return Objects.equals(id, post.id)
        && Objects.equals(title, post.title)
        && Objects.equals(content, post.content)
        && Objects.equals(tags, post.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, tags);
  }

  @Override
  public String toString() {
    return "Post{"
        + "id='"
        + id
        + '\''
        + ", title='"
        + title
        + '\''
        + ", content='"
        + content
        + '\''
        + ", tags="
        + tags
        + '}';
  }

  public static final class Builder {
    private String id;
    private String title;
    private String content;
    private List<String> tags;

    private Builder() {}

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder title(String val) {
      title = val;
      return this;
    }

    public Builder content(String val) {
      content = val;
      return this;
    }

    public Builder tags(List<String> val) {
      tags = val;
      return this;
    }

    public Post build() {
      return new Post(this);
    }
  }
}
