package com.yoloo.server.auth;

import org.springframework.security.core.Authentication;

public class AuthUtil {

  private AuthUtil() {}

  public static YolooUser from(Authentication authentication) {
    return (YolooUser) authentication.getDetails();
  }
}
