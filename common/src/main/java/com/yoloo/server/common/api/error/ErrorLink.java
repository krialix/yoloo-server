package com.yoloo.server.common.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ErrorLink {
  @JsonProperty("about")
  private String about;

  ErrorLink() {}

  ErrorLink(String about) {
    this.about = about;
  }

  public String getAbout() {
    return this.about;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ErrorLink errorLink = (ErrorLink) o;
    return Objects.equals(about, errorLink.about);
  }

  @Override
  public int hashCode() {
    return Objects.hash(about);
  }
}
