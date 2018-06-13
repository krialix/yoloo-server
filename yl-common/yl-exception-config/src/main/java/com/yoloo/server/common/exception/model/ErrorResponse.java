package com.yoloo.server.common.exception.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

  @JsonProperty
  private final List<Error> errors;

  @JsonCreator
  protected ErrorResponse(@JsonProperty("errors") List<Error> errors) {
    this.errors = errors;
  }

  public static ErrorResponse of(Error error) {
    return of(Collections.singletonList(error));
  }

  public static ErrorResponse of(List<Error> errors) {
    return new ErrorResponse(errors);
  }

  public List<Error> getErrors() {
    return this.errors;
  }
}
