package com.yoloo.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoloo.server.rest.error.vo.Error;
import com.yoloo.server.rest.error.vo.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultAuthenticationFailureHandler.class);

  private final ObjectMapper objectMapper;

  public DefaultAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    LOGGER.warn(exception.getMessage());

    HttpStatus httpStatus = translateAuthenticationException(exception);

    response.setStatus(httpStatus.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    writeResponse(response.getWriter(), httpStatus, exception);
  }

  protected HttpStatus translateAuthenticationException(AuthenticationException exception) {
    return HttpStatus.UNAUTHORIZED;
  }

  protected void writeResponse(
      Writer writer, HttpStatus httpStatus, AuthenticationException exception) throws IOException {
    Error error =
        Error.builder().error(httpStatus.getReasonPhrase()).message(exception.getMessage()).build();
    ErrorResponse errorResponse = ErrorResponse.of(error);
    objectMapper.writeValue(writer, errorResponse);
  }
}
