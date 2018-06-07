package com.yoloo.server.firebase.filter;

import com.google.common.base.Strings;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirebaseAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

  private static final String TOKEN_HEADER = "X-Authorization-Firebase";

  FirebaseAuthenticationTokenFilter() {
    super("/auth/**");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String authToken = request.getHeader(TOKEN_HEADER);

    if (!Strings.isNullOrEmpty(authToken)) {
      throw new AuthenticationServiceException("X-Authorization-Firebase token is not provided");
    }

    FirebaseAuthenticationToken authenticationToken = new FirebaseAuthenticationToken(authToken);

    return getAuthenticationManager().authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);

    // As this authentication is in HTTP header, after success we need to continue the request
    // normally and return the response as if the resource was not secured at all
    chain.doFilter(request, response);
  }
}
