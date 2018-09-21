package com.yoloo.server.auth.firebase;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class FirebaseAuthenticationToken extends UsernamePasswordAuthenticationToken {

  private final String idToken;

  private FirebaseAuthenticationToken(String idToken) {
    super(null, null);
    this.idToken = idToken;
  }

  static FirebaseAuthenticationToken fromFirebaseIdToken(String idToken) {
    return new FirebaseAuthenticationToken(idToken);
  }

  String getIdToken() {
    return idToken;
  }

  @Override
  public String toString() {
    return "FirebaseAuthenticationToken{" + "idToken='" + idToken + '\'' + '}';
  }
}
