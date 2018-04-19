package com.yoloo.server.common.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
  @JsonProperty("id")
  private String id;

  @JsonProperty("links")
  private ErrorLink links;

  @JsonProperty("status")
  private String httpStatusCode;

  @JsonProperty("code")
  private String applicationErrorCode;

  @JsonProperty("title")
  private String title;

  @JsonProperty("detail")
  private String detail;

  @JsonProperty("source")
  private ErrorSource source;

  private Error() {}

  private Error(
      String id,
      ErrorLink links,
      String httpStatusCode,
      String applicationErrorCode,
      String title,
      String detail,
      ErrorSource source) {
    this.id = id;
    this.links = links;
    this.httpStatusCode = httpStatusCode;
    this.applicationErrorCode = applicationErrorCode;
    this.title = title;
    this.detail = detail;
    this.source = source;
  }

  public static ErrorBuilder builder() {
    return new ErrorBuilder();
  }

  public String getId() {
    return this.id;
  }

  public ErrorLink getLinks() {
    return this.links;
  }

  public String getHttpStatusCode() {
    return this.httpStatusCode;
  }

  public String getApplicationErrorCode() {
    return this.applicationErrorCode;
  }

  public String getTitle() {
    return this.title;
  }

  public String getDetail() {
    return this.detail;
  }

  public ErrorSource getSource() {
    return this.source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Error error = (Error) o;
    return Objects.equals(id, error.id)
        && Objects.equals(links, error.links)
        && Objects.equals(httpStatusCode, error.httpStatusCode)
        && Objects.equals(applicationErrorCode, error.applicationErrorCode)
        && Objects.equals(title, error.title)
        && Objects.equals(detail, error.detail)
        && Objects.equals(source, error.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, links, httpStatusCode, applicationErrorCode, title, detail, source);
  }

  public static class ErrorBuilder {
    private String id;
    private ErrorLink links;
    private String httpStatusCode;
    private String applicationErrorCode;
    private String title;
    private String detail;
    private ErrorSource source;

    ErrorBuilder() {}

    public ErrorBuilder id(String id) {
      this.id = id;
      return this;
    }

    public ErrorBuilder links(ErrorLink links) {
      this.links = links;
      return this;
    }

    public ErrorBuilder httpStatusCode(String httpStatusCode) {
      this.httpStatusCode = httpStatusCode;
      return this;
    }

    public ErrorBuilder applicationErrorCode(String applicationErrorCode) {
      this.applicationErrorCode = applicationErrorCode;
      return this;
    }

    public ErrorBuilder title(String title) {
      this.title = title;
      return this;
    }

    public ErrorBuilder detail(String detail) {
      this.detail = detail;
      return this;
    }

    public ErrorBuilder source(ErrorSource source) {
      this.source = source;
      return this;
    }

    public Error build() {
      return new Error(id, links, httpStatusCode, applicationErrorCode, title, detail, source);
    }
  }
}
