package com.yoloo.server.auth.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Qualifier("yoloo2")
@Component
public class Yoloo2AuthProvider extends DaoAuthenticationProvider {

  public Yoloo2AuthProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    setUserDetailsService(userDetailsService);
    setPasswordEncoder(passwordEncoder);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    logger.info("Authenticating using Yoloo2AuthProvider");
    return super.authenticate(authentication);
  }
}
