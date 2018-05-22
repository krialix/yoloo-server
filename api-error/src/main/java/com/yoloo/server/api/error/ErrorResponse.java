package com.yoloo.server.api.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

  @JsonProperty private final List<Error> errors;

  @JsonCreator
  public ErrorResponse(@JsonProperty("errors") List<Error> errors) {
    this.errors = errors;
  }

  public ErrorResponse(Error error) {
    this(Collections.singletonList(error));
  }

  public List<Error> getErrors() {
    return this.errors;
  }
}
