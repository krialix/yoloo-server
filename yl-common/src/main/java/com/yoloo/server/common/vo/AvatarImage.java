package com.yoloo.server.common.vo;

import java.util.Objects;

public class AvatarImage implements ValueObject<AvatarImage> {

  private Url url;

  private AvatarImage() {
  }

  public AvatarImage(Url url) {
    this.url = url;
  }

  public Url getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvatarImage that = (AvatarImage) o;
    return Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }

  @Override
  public String toString() {
    return "AvatarImage{" + "url=" + url + '}';
  }

  @Override
  public boolean sameValueAs(AvatarImage other) {
    return equals(other);
  }
}
