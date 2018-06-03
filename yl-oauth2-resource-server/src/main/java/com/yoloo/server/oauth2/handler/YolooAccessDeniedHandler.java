package com.yoloo.server.oauth2.handler;

import com.yoloo.server.oauth2.exception.YolooOAuth2ExceptionRenderer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class YolooAccessDeniedHandler implements AccessDeniedHandler {

  private final WebResponseExceptionTranslator exceptionTranslator =
      new DefaultWebResponseExceptionTranslator();
  private final HandlerExceptionResolver handlerExceptionResolver =
      new DefaultHandlerExceptionResolver();
  private final OAuth2ExceptionRenderer exceptionRenderer = new YolooOAuth2ExceptionRenderer();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    try {
      final ResponseEntity<OAuth2Exception> result =
          exceptionTranslator.translate(accessDeniedException);
      exceptionRenderer.handleHttpEntityResponse(result, new ServletWebRequest(request, response));
      response.flushBuffer();
    } catch (final ServletException e) {
      if (handlerExceptionResolver.resolveException(request, response, this, e) == null) {
        throw e;
      }
    } catch (final IOException | RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
