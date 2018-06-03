package com.yoloo.server.oauth2.converter;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

public class YolooAccessTokenConverter extends DefaultAccessTokenConverter {

  @Override
  public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
    final OAuth2Authentication authentication = super.extractAuthentication(map);
    authentication.setDetails(map);
    return authentication;
  }
}
