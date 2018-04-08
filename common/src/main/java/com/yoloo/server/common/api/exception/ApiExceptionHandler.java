package com.yoloo.server.common.api.exception;

import org.dialectic.jsonapi.error.Error;
import org.dialectic.jsonapi.error.ErrorResponse;
import org.dialectic.jsonapi.response.JsonApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
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

  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  private final MessageSource messageSource;

  @Autowired
  ApiExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  private static String getI18NMessage(
      MessageSource messageSource, String code, Object[] args, Locale locale) {
    String message;
    try {
      message = messageSource.getMessage(code, args, locale);
    } catch (NoSuchMessageException e) {
      log.error("Couldn't find any message for {} code under {} locale", code, locale);
      message = messageSource.getMessage("error.default.message_not_found", args, locale);
    }

    return message;
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ErrorResponse<Error>> handleServiceExceptions(
      ServiceException ex, Locale locale) {

    Error error =
        Error.builder()
            .applicationErrorCode(ex.getApplicationErrorCode())
            .httpStatusCode(ex.getHttpStatus().toString())
            .detail(messageSource.getMessage(ex.getApplicationErrorCode(), null, locale))
            .build();

    return ResponseEntity.status(ex.getHttpStatus()).body(JsonApi.error(error));
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
                        .detail(messageSource.getMessage(objectError, locale))
                        .build())
            .collect(toList());

    return ResponseEntity.badRequest().body(JsonApi.error(apiErrors));
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
                                cv.getMessage(), cv.getExecutableParameters(), locale))
                        .build())
            .collect(toList());

    return ResponseEntity.badRequest().body(JsonApi.error(apiErrors));
  }

  @ExceptionHandler(HttpMediaTypeException.class)
  public ResponseEntity<ErrorResponse<Error>> handleMediaTypeExceptions() {
    Error error =
        Error.builder()
            .applicationErrorCode("demo")
            .httpStatusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString())
            .detail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())
            .build();

    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(JsonApi.error(error));
  }
}