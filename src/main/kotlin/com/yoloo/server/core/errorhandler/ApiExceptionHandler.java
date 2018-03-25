package com.yoloo.server.core.errorhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Exception handler that catches all exceptions thrown by the REST layer
 * and convert them to the appropriate {@linkplain ErrorResponse}s with a
 * suitable HTTP status code.
 *
 * @author Ali Dehghani
 * @see ErrorCode
 * @see ErrorCodeFactory
 * @see ErrorResponse
 */
@ControllerAdvice
class ApiExceptionHandler {
  private static final String NO_MESSAGE_AVAILABLE = "No message available";

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

  /**
   * Factory to convert the given {@linkplain Exception} to an instance of
   * {@linkplain ErrorCode}
   */
  private final ErrorCodeFactory errorCodeFactory;

  /**
   * Responsible for finding the appropriate error message(s) based on the given
   * {@linkplain ErrorCode} and {@linkplain Locale}
   */
  private final MessageSource messageSource;

  /**
   * Construct a valid instance of the exception handler
   *
   * @throws NullPointerException If either of required parameters were {@code null}
   */
  @Autowired
  ApiExceptionHandler(ErrorCodeFactory errorCodeFactory, MessageSource messageSource) {
    Objects.requireNonNull(errorCodeFactory);
    Objects.requireNonNull(messageSource);

    this.errorCodeFactory = errorCodeFactory;
    this.messageSource = messageSource;
  }

  /**
   * Convert the passed {@code errorCode} to an instance of
   * {@linkplain ErrorResponse} using the given {@code locale}
   */
  private static ErrorResponse.ApiError toApiError(MessageSource messageSource, ErrorCode errorCode, Locale locale) {
    String message;
    try {
      message = messageSource.getMessage(errorCode.getCode(), new Object[]{}, locale);
    } catch (NoSuchMessageException e) {
      LOGGER.error("Couldn't find any message for {} code under {} locale", errorCode.getCode(), locale);
      message = NO_MESSAGE_AVAILABLE;
    }

    return ErrorResponse.ApiError.of(errorCode, message);
  }

  /**
   * Catches all non-validation exceptions and tries to convert them to
   * appropriate HTTP Error responses
   *
   * <p>First using the {@linkplain #errorCodeFactory} will find the corresponding
   * {@linkplain ErrorCode} for the given {@code exception}. Then based on
   * the resolved {@linkplain Locale}, a suitable instance of
   * {@linkplain ErrorResponse} with appropriate and localized message will
   * return to the client. {@linkplain ErrorCode} itself determines the HTTP
   * status of the response.
   *
   * @param exception The exception to convert
   * @param locale    The locale that usually resolved by {@code Accept-Language}
   *                  header. This locale will determine the language of the returned error
   *                  message.
   * @return An appropriate HTTP Error Response with suitable status code
   * and error messages
   */
  @ExceptionHandler(ServiceException.class)
  ResponseEntity<ErrorResponse> handleExceptions(ServiceException exception, Locale locale) {
    ErrorCode errorCode = errorCodeFactory.of(exception);
    ErrorResponse.ApiError apiError = toApiError(messageSource, errorCode, locale);
    ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), apiError);

    return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
  }

  /**
   * Catches all validation exceptions and render appropriate error responses based on each
   * validation exception
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception,
                                                                  Locale locale) {
    List<ErrorResponse.ApiError> apiErrors = exception
        .getBindingResult()
        .getAllErrors()
        .stream()
        .map(ObjectError::getDefaultMessage)
        .map(ErrorCode::validation)
        .map(code -> toApiError(messageSource, code, locale))
        .collect(toList());

    return ResponseEntity.badRequest().body(ErrorResponse.ofErrors(HttpStatus.BAD_REQUEST, apiErrors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions2(ConstraintViolationException exception,
                                                                   Locale locale) {
    List<ErrorResponse.ApiError> apiErrors = exception
        .getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .map(ErrorCode::validation)
        .map(code -> toApiError(messageSource, code, locale))
        .collect(toList());

    return ResponseEntity.badRequest().body(ErrorResponse.ofErrors(HttpStatus.BAD_REQUEST, apiErrors));
  }
}
