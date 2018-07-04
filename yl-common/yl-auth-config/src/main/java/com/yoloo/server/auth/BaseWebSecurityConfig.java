package com.yoloo.server.auth;

import com.yoloo.server.auth.firebase.FirebaseAuthenticationProvider;
import com.yoloo.server.auth.firebase.FirebaseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public abstract class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final FirebaseAuthenticationProvider firebaseAuthenticationProvider;

  @Autowired
  public BaseWebSecurityConfig(FirebaseAuthenticationProvider firebaseAuthenticationProvider) {
    this.firebaseAuthenticationProvider = firebaseAuthenticationProvider;
  }

  private static FirebaseFilter firebaseFilter(AuthenticationManager authenticationManager) {
    return FirebaseFilter.create(authenticationManager);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(firebaseAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic()
        .disable()
        .addFilterBefore(
            firebaseFilter(authenticationManagerBean()),
            UsernamePasswordAuthenticationFilter.class);
  }
}
