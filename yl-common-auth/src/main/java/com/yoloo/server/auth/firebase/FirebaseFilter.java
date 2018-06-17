package com.yoloo.server.auth.firebase;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirebaseFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseFilter.class);

  private static final String TOKEN_HEADER = "X-Auth-Token";

  private final AuthenticationManager authenticationManager;

  private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();

  private FirebaseFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public static FirebaseFilter create(AuthenticationManager authenticationManager) {
    return new FirebaseFilter(authenticationManager);
  }

  @Override
  public void afterPropertiesSet() throws ServletException {
    super.afterPropertiesSet();
    Assert.notNull(authenticationManager, "authenticationManager must be specified");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String idToken = request.getHeader(TOKEN_HEADER);

    if (!requiresAuthentication(request)) {
      LOGGER.info("{} is not found. Moving on next chain.", TOKEN_HEADER);
      chain.doFilter(request, response);
    } else {
      LOGGER.info("{} is found. SecurityContextHolder filling with token.", TOKEN_HEADER);
      Authentication authentication = attempAuthentication(idToken);
      if (authentication == null) {
        // return immediately as subclass has indicated that it hasn't completed
        // authentication
        return;
      }
      sessionStrategy.onAuthentication(authentication, request, response);
      chain.doFilter(request, response);

      successfulAuthentication(request, response, chain, authentication);
    }
  }

  private Authentication attempAuthentication(String idToken) {
    FirebaseAuthenticationToken authenticationToken =
        FirebaseAuthenticationToken.fromFirebaseIdToken(idToken);

    return authenticationManager.authenticate(authenticationToken);
  }

  private boolean requiresAuthentication(HttpServletRequest request) {
    String idToken = request.getHeader(TOKEN_HEADER);
    return !Strings.isNullOrEmpty(idToken);
  }

  private void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "Authentication success. Updating SecurityContextHolder to contain: {}", authResult);
    }

    SecurityContextHolder.getContext().setAuthentication(authResult);
  }

  /*@Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String idToken = TokenUtil.extractTokenFromRequest(request);
    LOGGER.info(TokenUtil.TOKEN_HEADER + ": {}", idToken);

    FirebaseAuthenticationToken authenticationToken =
        FirebaseAuthenticationToken.fromFirebaseIdToken(idToken);

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
    LOGGER.info("successfulAuthentication() continue on chain");

    // As this authentication is in HTTP header, after success we need to continue the request
    // normally and return the response as if the resource was not secured at all
    chain.doFilter(request, response);
  }*/
}
