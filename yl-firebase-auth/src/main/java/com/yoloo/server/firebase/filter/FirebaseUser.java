package com.yoloo.server.firebase.filter;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class FirebaseUser implements UserDetails {

  private final FirebaseToken token;

  public FirebaseUser(FirebaseToken token) {
    this.token = token;
  }

  public Long getUserId() {
    return Long.parseLong(token.getUid());
  }

  public String getPicture() {
    return token.getPicture();
  }

  public String getEmail() {
    return token.getEmail();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return token.getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
