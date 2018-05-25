package com.yoloo.server.api.handler;

import com.yoloo.server.api.error.Error;
import com.yoloo.server.api.error.ErrorResponse;
import com.yoloo.server.api.exception.ServiceException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Exception handler that catches all exceptions thrown by the REST layer and convert them to the
 * appropriate {@linkplain ResponseEntity}s with a suitable HTTP status code.
 */
@ControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  ApiExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<Object> handleServiceExceptions(ServiceException ex, WebRequest request) {
    Error error =
        buildError(
            ex.getHttpStatus(),
            messageSource.getMessage(ex.getMessage(), ex.getArgs(), request.getLocale()));

    return handleExceptionInternal(ex, new ErrorResponse(error), null, ex.getHttpStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    pageNotFoundLogger.warn(ex.getMessage());

    Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
    if (!CollectionUtils.isEmpty(supportedMethods)) {
      headers.setAllow(supportedMethods);
    }

    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
    if (!CollectionUtils.isEmpty(mediaTypes)) {
      headers.setAccept(mediaTypes);
    }

    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
      ServletRequestBindingException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(
      ConversionNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(
      HttpMessageNotWritableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    BindingResult result = ex.getBindingResult();

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

    return handleExceptionInternal(ex, new ErrorResponse(errors), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, request);
  }

  private Error buildError(HttpStatus status, String message) {
    return Error.builder().error(status.getReasonPhrase()).message(message).build();
  }

  @Override
  protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
      AsyncRequestTimeoutException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest webRequest) {
    if (webRequest instanceof ServletWebRequest) {
      ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
      HttpServletRequest request = servletWebRequest.getRequest();
      HttpServletResponse response = servletWebRequest.getResponse();
      if (response != null && response.isCommitted()) {
        if (logger.isDebugEnabled()) {
          logger.debug(
              "Async timeout for " + request.getMethod() + " [" + request.getRequestURI() + "]");
        }
        return null;
      }
    }

    Error error = buildError(status, ex.getMessage());

    return handleExceptionInternal(ex, new ErrorResponse(error), headers, status, webRequest);
  }

  @ExceptionHandler(Exception.class)
  public void handleException(Exception e) {
    logger.info("TYPE: {}", e);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    Error error = buildError(status, ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(error);
    return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
  }
}
