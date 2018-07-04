package com.yoloo.server.common.exception.handler;

import com.yoloo.server.common.exception.exception.ServiceException;
import com.yoloo.server.common.exception.model.Error;
import com.yoloo.server.common.exception.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception handler that catches all exceptions thrown by the REST layer and convert them to the
 * appropriate {@linkplain ResponseEntity}s with a suitable HTTP status code.
 */
@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<Object> handleServiceExceptions(
      ServiceException ex, HttpHeaders headers, WebRequest request) {
    return handleExceptionInternal(ex, null, headers, ex.getHttpStatus(), request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationExceptions(
      ConstraintViolationException ex) {
    return handleExceptionInternal(ex, null, null, HttpStatus.BAD_REQUEST, null);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

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
    } else if (ex instanceof ConstraintViolationException) {
      ConstraintViolationException exception = (ConstraintViolationException) ex;

      List<Error> errors =
          exception
              .getConstraintViolations()
              .stream()
              .map(
                  violation -> {
                    String dumbFieldName = violation.getPropertyPath().toString();
                    String fieldName = dumbFieldName.substring(dumbFieldName.indexOf(".") + 1);

                    return Error.builder()
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(violation.getMessage())
                        .field(fieldName)
                        .rejectedValue(violation.getInvalidValue())
                        .build();
                  })
              .collect(Collectors.toList());

      ErrorResponse errorResponse = ErrorResponse.of(errors);
      return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    Error error = Error.builder().error(status.getReasonPhrase()).message(ex.getMessage()).build();
    ErrorResponse errorResponse = ErrorResponse.of(error);
    return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
  }
}
