package com.yoloo.server.firebase.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

@Configuration
public class FirebaseAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private final FirebaseAuth firebaseAuth;

  @Autowired
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
    try {
      FirebaseToken token = firebaseAuth.verifyIdToken(authenticationToken.getToken(), true);
      return new FirebaseUser(token);
    } catch (FirebaseAuthException e) {
      throw new SessionAuthenticationException(e.getMessage());
    }
  }
}
