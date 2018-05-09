package com.yoloo.server.auth.domain.vo;

import com.yoloo.server.auth.domain.entity.OauthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class OauthUserDetails implements UserDetails {

  private final OauthUser user;

  public OauthUserDetails(OauthUser user) {
    this.user = user;
  }

  public long getUserId() {
    return Long.parseLong(user.getId().substring(6));
  }

  public String avatarUrl() {
    return user.getImage().getUrl().getValue();
  }

  public String getEmail() {
    return user.getEmail().getValue();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getScopes().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return user.getPassword() == null ? null : user.getPassword().getValue();
  }

  @Override
  public String getUsername() {
    return user.getDisplayName().getValue();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.getExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.getLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !user.getCredentialsExpired();
  }

  @Override
  public boolean isEnabled() {
    return !user.getDisabled();
  }
}
