package com.yoloo.server.common.vo;

import java.util.Objects;

public class Url {
  private String value;

  private Url() {}

  public Url(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Url url = (Url) o;
    return Objects.equals(value, url.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "Url{" + "value='" + value + '\'' + '}';
  }
}
