package com.yoloo.server.common.api.handler;

import com.yoloo.server.common.api.error.Error;
import com.yoloo.server.common.api.error.ErrorResponse;
import com.yoloo.server.common.api.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * Exception handler that catches all exceptions thrown by the REST layer and convert them to the
 * appropriate {@linkplain ResponseEntity}s with a suitable HTTP status code.
 */
@ControllerAdvice
class ApiExceptionHandler {

  private static final String UNKNOWN_ERROR_CODE = "unknown error code";

  private final MessageSource messageSource;

  @Autowired
  ApiExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ErrorResponse<Error>> handleServiceExceptions(
      ServiceException ex, Locale locale) {

    Error error =
        Error.builder()
            .applicationErrorCode(ex.getApplicationErrorCode())
            .httpStatusCode(ex.getHttpStatus().toString())
            .detail(
                messageSource.getMessage(
                    ex.getApplicationErrorCode(), null, UNKNOWN_ERROR_CODE, locale))
            .build();

    return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse<>(error));
  }

  /**
   * Catches all validation exceptions and render appropriate error responses based on each
   * validation ex
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse<Error>> handleMethodArgumentValidationExceptions(
      MethodArgumentNotValidException ex, Locale locale) {
    List<Error> apiErrors =
        ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(
                objectError ->
                    Error.builder()
                        .applicationErrorCode(objectError.getDefaultMessage())
                        .httpStatusCode(HttpStatus.BAD_REQUEST.toString())
                        .detail(
                            messageSource.getMessage(
                                objectError.getDefaultMessage(),
                                objectError.getArguments(),
                                UNKNOWN_ERROR_CODE,
                                locale))
                        .build())
            .collect(toList());

    return ResponseEntity.badRequest().body(new ErrorResponse<>(apiErrors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse<Error>> handleConstraintValidationExceptions(
      ConstraintViolationException ex, Locale locale) {
    List<Error> apiErrors =
        ex.getConstraintViolations()
            .stream()
            .map(
                cv ->
                    Error.builder()
                        .applicationErrorCode(cv.getMessage())
                        .httpStatusCode(HttpStatus.BAD_REQUEST.toString())
                        .detail(
                            messageSource.getMessage(
                                cv.getMessageTemplate(),
                                cv.getExecutableParameters(),
                                UNKNOWN_ERROR_CODE,
                                locale))
                        .build())
            .collect(toList());

    return ResponseEntity.badRequest().body(new ErrorResponse<>(apiErrors));
  }

  @ExceptionHandler({HttpMediaTypeException.class, HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ErrorResponse<Error>> handleMediaTypeExceptions() {
    Error error =
        Error.builder()
            .applicationErrorCode("demo")
            .httpStatusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString())
            .detail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())
            .build();

    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .body(new ErrorResponse<>(error));
  }
}
