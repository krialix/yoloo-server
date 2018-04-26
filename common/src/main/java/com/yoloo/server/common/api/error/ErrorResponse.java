package com.yoloo.server.common.api.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

  @JsonProperty private final List<Error> errors;

  @JsonProperty("path")
  private final String path;

  @JsonCreator
  public ErrorResponse(@JsonProperty("errors") List<Error> errors, String path) {
    this.errors = errors;
    this.path = path;
  }

  public ErrorResponse(Error error, String path) {
    this(Collections.singletonList(error), path);
  }

  public List<Error> getErrors() {
    return this.errors;
  }

  public String getPath() {
    return path;
  }
}
