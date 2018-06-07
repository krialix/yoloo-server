package com.yoloo.server.firebase.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] ALLOWED_ENDPOINTS =
      new String[] {"/_ah/**", "/api/pubsub/**", "/api/*/users/auth"};

  private static final String[] AUTHENTICATED_ENDPOINTS = new String[] {"/api/**"};

  private final FirebaseAuthenticationProvider firebaseAuthenticationProvider;

  @Autowired
  public WebSecurityConfig(FirebaseAuthenticationProvider firebaseAuthenticationProvider) {
    this.firebaseAuthenticationProvider = firebaseAuthenticationProvider;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(firebaseAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic()
        .disable()
        .authorizeRequests()
        .antMatchers(ALLOWED_ENDPOINTS)
        .permitAll()
        .and()
        .authorizeRequests()
        .antMatchers(AUTHENTICATED_ENDPOINTS)
        .fullyAuthenticated()
        .and()
        .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  private FirebaseAuthenticationTokenFilter authenticationTokenFilter() throws Exception {
    FirebaseAuthenticationTokenFilter filter = new FirebaseAuthenticationTokenFilter();
    filter.setAuthenticationManager(authenticationManager());
    return filter;
  }
}
