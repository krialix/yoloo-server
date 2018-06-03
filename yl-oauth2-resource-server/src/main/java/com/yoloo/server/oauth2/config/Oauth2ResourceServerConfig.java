package com.yoloo.server.oauth2.config;

import com.yoloo.server.oauth2.converter.YolooAccessTokenConverter;
import com.yoloo.server.oauth2.handler.YolooAccessDeniedHandler;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableResourceServer
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

  private final ResourceLoader resourceLoader;
  private final String publicKeyPath;

  public Oauth2ResourceServerConfig(
      ResourceLoader resourceLoader,
      @Value("${security.oauth2.path.public_key}") String publicKeyPath) {
    this.resourceLoader = resourceLoader;
    this.publicKeyPath = publicKeyPath;
  }

  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) throws IOException {
    resources.tokenServices(tokenServices()).accessDeniedHandler(new YolooAccessDeniedHandler());
  }

  @Bean
  public TokenStore tokenStore() throws IOException {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() throws IOException {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(new YolooAccessTokenConverter());
    converter.setVerifierKey(getVerifierKey());
    return converter;
  }

  @Bean
  public DefaultTokenServices tokenServices() throws IOException {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    return defaultTokenServices;
  }

  private String getVerifierKey() throws IOException {
    final Resource publicKeyResource = resourceLoader.getResource(publicKeyPath);
    return IOUtils.toString(publicKeyResource.getInputStream(), StandardCharsets.UTF_8);
  }
}
