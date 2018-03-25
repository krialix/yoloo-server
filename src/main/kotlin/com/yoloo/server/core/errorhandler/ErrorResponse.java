package com.yoloo.server.core.errorhandler;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

/**
 * An immutable data structure representing HTTP error response bodies. JSON
 * representation of this class would be something like the following:
 * <pre>
 *     {
 *         "status_code": 404,
 *         "reason_phrase": "Not Found",
 *         "errors": [
 *             {"code": "res-15", "message": "some error message"},
 *             {"code": "res-16", "message": "yet another message"}
 *         ]
 *     }
 * </pre>
 *
 * @author Ali Dehghani
 */
class ErrorResponse {
  /**
   * The 4xx or 5xx status code for error cases, e.g. 404
   */
  private final int statusCode;

  /**
   * The HTTP reason phrase corresponding the {@linkplain #statusCode},
   * e.g. Not Found
   *
   * @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html">
   * Status Code and Reason Phrase</a>
   */
  private final String reasonPhrase;

  /**
   * List of application-level error code and message combinations.
   * Using these errors we provide more information about the
   * actual error
   */
  private final List<ApiError> errors;

  private ErrorResponse(int statusCode, String reason, List<ApiError> errors) {
    this.statusCode = statusCode;
    this.reasonPhrase = reason;
    this.errors = errors;
  }

  /**
   * Static factory method to create a {@linkplain ErrorResponse} with multiple
   * {@linkplain ApiError}s. The canonical use case of this factory method is when
   * we're handling validation exceptions, since we may have multiple validation
   * errors.
   */
  static ErrorResponse ofErrors(HttpStatus status, List<ApiError> errors) {
    return new ErrorResponse(status.value(), status.getReasonPhrase(), errors);
  }

  /**
   * Static factory method to create a {@linkplain ErrorResponse} with a single
   * {@linkplain ApiError}. The canonical use case for this method is when we trying
   * to create {@linkplain ErrorResponse}es for regular non-validation exceptions.
   */
  static ErrorResponse of(HttpStatus status, ApiError error) {
    return ofErrors(status, Collections.singletonList(error));
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  public List<ApiError> getErrors() {
    return errors;
  }

  /**
   * An immutable data structure representing each application-level error. JSON
   * representation of this class would be something like the following:
   * <pre>
   *     {"code": "res-12", "message": "some error"}
   * </pre>
   *
   * @author Ali Dehghani
   */
  static class ApiError {
    /**
     * The error code
     */
    private final String code;

    /**
     * Possibly localized error message
     */
    private final String message;

    private ApiError(String code, String message) {
      this.code = code;
      this.message = message;
    }

    public static ApiError of(ErrorCode code, String message) {
      return of(code.getCode(), message);
    }

    public static ApiError of(String code, String message) {
      return new ApiError(code, message);
    }

    public String getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }
  }
}
