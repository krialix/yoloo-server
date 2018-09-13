package com.yoloo.server.auth;

import org.springframework.security.core.Authentication;

public class AuthUtil {

  private AuthUtil() {}

  public static AuthenticatedUser from(Authentication authentication) {
    return (AuthenticatedUser) authentication.getDetails();
  }
}
