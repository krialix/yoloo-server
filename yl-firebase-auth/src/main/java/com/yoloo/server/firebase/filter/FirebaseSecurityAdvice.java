package com.yoloo.server.firebase.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FirebaseSecurityAdvice {

  @ModelAttribute
  public FirebaseUser currentUser() {
    Authentication originAuthentication = SecurityContextHolder.getContext().getAuthentication();
    if (originAuthentication == null) {
      return null;
    }
    if (!originAuthentication.isAuthenticated()) {
      return null;
    }
    return (FirebaseUser) originAuthentication.getDetails();
  }
}
