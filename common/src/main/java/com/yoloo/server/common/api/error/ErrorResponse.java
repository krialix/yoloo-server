package com.yoloo.server.common.api.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse<T extends Error> {

  @JsonProperty private final List<T> errors;

  @JsonCreator
  public ErrorResponse(@JsonProperty("errors") List<T> errors) {
    this.errors = errors;
  }

  public ErrorResponse(T error) {
    this(Collections.singletonList(error));
  }

  public List<T> getErrors() {
    return this.errors;
  }
}
