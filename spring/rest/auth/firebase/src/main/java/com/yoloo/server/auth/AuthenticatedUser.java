package com.yoloo.server.auth;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedUser implements UserDetails {

  private final FirebaseToken token;
  private final List<GrantedAuthority> authorities;

  private AuthenticatedUser(FirebaseToken token) {
    this.token = token;
    this.authorities = extractAuthorities(token);
  }

  private static List<GrantedAuthority> extractAuthorities(FirebaseToken token) {
    //noinspection unchecked
    List<String> roles =
        (List<String>) token.getClaims().getOrDefault("roles", Collections.emptyList());
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  public static AuthenticatedUser fromToken(FirebaseToken token) {
    return new AuthenticatedUser(token);
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
    return authorities;
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
