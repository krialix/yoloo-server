package com.yoloo.server.oauth2.factory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Component
public class TokenFactory implements InitializingBean {

  private final ClientDetailsService clientDetailsService;
  private final ResourceLoader resourceLoader;

  @Value("${security.oauth2.key_store.path}")
  private String keyStorePath;

  @Value("${security.oauth2.key_pair_alias}")
  private String keyStoreAlias;

  @Value("${security.oauth2.key_store.password}")
  private String keyStorePassword;

  private AuthorizationServerTokenServices tokenServices;

  public TokenFactory(
      final ClientDetailsService clientDetailsService, final ResourceLoader resourceLoader) {
    this.clientDetailsService = clientDetailsService;
    this.resourceLoader = resourceLoader;
  }

  public RequestPostProcessor token(final String userId, final String scope) {
    return token(userId, scope, null);
  }

  public RequestPostProcessor token(final String userId, final String scope, final String role) {
    return mockRequest -> {
      final OAuth2AccessToken token = createAccessToken(mockRequest, userId, "test", scope, role);
      mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
      return mockRequest;
    };
  }

  private OAuth2AccessToken createAccessToken(
      final HttpServletRequest request,
      final String userId,
      final String clientId,
      final String scope,
      final String role) {
    final ClientDetails client = clientDetailsService.loadClientByClientId(clientId);

    final OAuth2Request oAuth2Request =
        new OAuth2Request(
            Collections.emptyMap(),
            clientId,
            client.getAuthorities(),
            true,
            Collections.singleton(scope),
            client.getResourceIds(),
            null,
            Collections.emptySet(),
            Collections.emptyMap());

    final Collection<GrantedAuthority> userAuthorities =
        role == null
            ? client.getAuthorities()
            : Collections.singletonList(new SimpleGrantedAuthority(role));
    final User userPrincipal = new User("user", "", true, true, true, true, userAuthorities);
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userPrincipal, null, userAuthorities);
    final OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
    final OAuth2AuthenticationDetails details = new OAuth2AuthenticationDetails(request);
    auth.setDetails(details);

    details.setDecodedDetails(Collections.singletonMap("user_id", userId));

    return tokenServices.createAccessToken(auth);
  }

  @Override
  public void afterPropertiesSet() {
    this.tokenServices = tokenServices();
  }

  private DefaultTokenServices tokenServices() {
    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
    defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  private TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  private JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    final KeyStoreKeyFactory keyStoreKeyFactory =
        new KeyStoreKeyFactory(
            resourceLoader.getResource(keyStorePath), keyStorePassword.toCharArray());
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyStoreAlias));
    return converter;
  }

  @SuppressWarnings("unchecked")
  private TokenEnhancer tokenEnhancer() {
    return (accessToken, authentication) -> {
      final OAuth2AuthenticationDetails oauth2Details =
          (OAuth2AuthenticationDetails) authentication.getDetails();
      ((DefaultOAuth2AccessToken) accessToken)
          .setAdditionalInformation((Map<String, Object>) oauth2Details.getDecodedDetails());
      return accessToken;
    };
  }
}
