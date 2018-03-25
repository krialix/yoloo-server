package com.yoloo.server.core.errorhandler;

import org.springframework.http.HttpStatus;

/**
 * Represents API error code. Each API should implement this interface to
 * provide an error code for each error case.
 *
 * @author Ali Dehghani
 * @implNote Enum implementations are good fit for this scenario.
 */
public class ErrorCode {
  /**
   * Represents the error code.
   *
   * @return The resource based error code
   */
  private final String code;

  /**
   * The corresponding HTTP status for the given error code
   *
   * @return Corresponding HTTP status code, e.g. 400 Bad Request for a validation
   * error code
   */
  private final HttpStatus httpStatus;

  private ErrorCode(String code, HttpStatus httpStatus) {
    this.code = code;
    this.httpStatus = httpStatus;
  }

  public static ErrorCode of(String code, HttpStatus httpStatus) {
    return new ErrorCode(code, httpStatus);
  }

  public static ErrorCode validation(String errorCode) {
    return new ErrorCode(errorCode, HttpStatus.BAD_REQUEST);
  }

  /**
   * Default implementation representing the Unknown Error Code. When the
   * {@linkplain ErrorCodeFactory} couldn't find any appropriate
   * {@linkplain ErrorCode} for any given {@linkplain Exception}, it will
   * use this implementation by default.
   */
  public static ErrorCode unknown() {
    return new ErrorCode("unknown", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public String getCode() {
    return code;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
