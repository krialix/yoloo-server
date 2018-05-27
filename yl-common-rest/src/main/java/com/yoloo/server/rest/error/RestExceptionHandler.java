package com.yoloo.server.rest.error;

import com.yoloo.server.rest.error.vo.Error;
import com.yoloo.server.rest.error.vo.ErrorResponse;
import com.yoloo.server.rest.error.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception handler that catches all exceptions thrown by the REST layer and convert them to the
 * appropriate {@linkplain ResponseEntity}s with a suitable HTTP status code.
 */
@ControllerAdvice
class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<Object> handleServiceExceptions(
      ServiceException ex, HttpHeaders headers, WebRequest request) {
    return handleExceptionInternal(ex, null, headers, ex.getHttpStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOGGER.info("Exception Type: {}", ex.getClass());

    if (ex instanceof MethodArgumentNotValidException) {
      BindingResult result = ((MethodArgumentNotValidException) ex).getBindingResult();

      List<Error> errors =
          result
              .getFieldErrors()
              .stream()
              .map(
                  objectError ->
                      Error.builder()
                          .error(status.getReasonPhrase())
                          .message(objectError.getDefaultMessage())
                          .field(objectError.getField())
                          .rejectedValue(objectError.getRejectedValue())
                          .build())
              .collect(Collectors.toList());

      ErrorResponse errorResponse = ErrorResponse.of(errors);
      return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    Error error = Error.builder().error(status.getReasonPhrase()).message(ex.getMessage()).build();
    ErrorResponse errorResponse = ErrorResponse.of(error);
    return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
  }
}
