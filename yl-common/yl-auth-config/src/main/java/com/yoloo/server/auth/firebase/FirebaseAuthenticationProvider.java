package com.yoloo.server.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.yoloo.server.auth.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class FirebaseAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(FirebaseAuthenticationProvider.class);

  private final FirebaseAuth firebaseAuth;

  public FirebaseAuthenticationProvider(FirebaseAuth firebaseAuth) {
    this.firebaseAuth = firebaseAuth;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return FirebaseAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {}

  @Override
  protected UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    FirebaseAuthenticationToken authenticationToken = (FirebaseAuthenticationToken) authentication;
    LOGGER.info("Retrieve user details: {}", authenticationToken);
    try {
      FirebaseToken token = firebaseAuth.verifyIdToken(authenticationToken.getIdToken(), true);
      LOGGER.info("FirebaseToken: {}", token);
      return AuthenticatedUser.fromToken(token);
    } catch (FirebaseAuthException e) {
      throw new UsernameNotFoundException(e.getMessage());
    }
  }
}
