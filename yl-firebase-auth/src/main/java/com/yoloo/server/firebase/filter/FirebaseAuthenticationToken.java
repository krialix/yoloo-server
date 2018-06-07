package com.yoloo.server.firebase.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class FirebaseAuthenticationToken extends UsernamePasswordAuthenticationToken {

  private final String token;

  public FirebaseAuthenticationToken(String token) {
    super(null, null);
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
