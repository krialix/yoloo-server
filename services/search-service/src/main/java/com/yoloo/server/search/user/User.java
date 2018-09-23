package com.yoloo.server.search.user;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Objects;

@SolrDocument(collection = "user")
public class User {

  @Id private String id;

  @Field private String displayName;

  private User() {}

  private User(Builder builder) {
    id = builder.id;
    displayName = builder.displayName;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return newBuilder().id(id).displayName(displayName);
  }

  public String getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(displayName, user.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, displayName);
  }

  public static final class Builder {
    private String id;
    private String displayName;

    private Builder() {}

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder displayName(String val) {
      displayName = val;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
