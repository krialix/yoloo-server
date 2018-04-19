package com.yoloo.server.common.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ErrorSource {
  @JsonProperty("pointer")
  private String pointer;

  @JsonProperty("parameter")
  private String parameter;

  private ErrorSource() {}

  private ErrorSource(String pointer, String parameter) {
    this.pointer = pointer;
    this.parameter = parameter;
  }

  public static ErrorSource of(String pointer, String parameter) {
    return new ErrorSource(pointer, parameter);
  }

  public String getPointer() {
    return this.pointer;
  }

  public void setPointer(String pointer) {
    this.pointer = pointer;
  }

  public String getParameter() {
    return this.parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ErrorSource that = (ErrorSource) o;
    return Objects.equals(pointer, that.pointer) && Objects.equals(parameter, that.parameter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pointer, parameter);
  }
}
