package com.yoloo.server.auth.domain.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.Jackson2ArrayOrStringDeserializer;
import org.springframework.security.oauth2.provider.client.JacksonArrayOrStringDeserializer;
import org.springframework.util.StringUtils;

import java.util.*;

@Cache
@Entity
public class Oauth2Client implements ClientDetails {

  @Id
  @org.codehaus.jackson.annotate.JsonProperty("client_id")
  @com.fasterxml.jackson.annotation.JsonProperty("client_id")
  private String clientId;

  @org.codehaus.jackson.annotate.JsonProperty("client_secret")
  @com.fasterxml.jackson.annotation.JsonProperty("client_secret")
  private String clientSecret;

  @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(
    using = Jackson2ArrayOrStringDeserializer.class
  )
  private Set<String> scope = Collections.emptySet();

  @org.codehaus.jackson.annotate.JsonProperty("resource_ids")
  @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
  @com.fasterxml.jackson.annotation.JsonProperty("resource_ids")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(
    using = Jackson2ArrayOrStringDeserializer.class
  )
  private Set<String> resourceIds = Collections.emptySet();

  @org.codehaus.jackson.annotate.JsonProperty("authorized_grant_types")
  @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
  @com.fasterxml.jackson.annotation.JsonProperty("authorized_grant_types")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(
    using = Jackson2ArrayOrStringDeserializer.class
  )
  private Set<String> authorizedGrantTypes = Collections.emptySet();

  @org.codehaus.jackson.annotate.JsonProperty("redirect_uri")
  @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
  @com.fasterxml.jackson.annotation.JsonProperty("redirect_uri")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(
    using = Jackson2ArrayOrStringDeserializer.class
  )
  private Set<String> registeredRedirectUris;

  @org.codehaus.jackson.annotate.JsonProperty("autoapprove")
  @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
  @com.fasterxml.jackson.annotation.JsonProperty("autoapprove")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(
    using = Jackson2ArrayOrStringDeserializer.class
  )
  private Set<String> autoApproveScopes;

  private List<GrantedAuthority> authorities = Collections.emptyList();

  @org.codehaus.jackson.annotate.JsonProperty("access_token_validity")
  @com.fasterxml.jackson.annotation.JsonProperty("access_token_validity")
  private Integer accessTokenValiditySeconds;

  @org.codehaus.jackson.annotate.JsonProperty("refresh_token_validity")
  @com.fasterxml.jackson.annotation.JsonProperty("refresh_token_validity")
  private Integer refreshTokenValiditySeconds;

  @org.codehaus.jackson.annotate.JsonIgnore @com.fasterxml.jackson.annotation.JsonIgnore
  private Map<String, Object> additionalInformation = new LinkedHashMap<>();

  private Oauth2Client() {}

  public Oauth2Client(ClientDetails prototype) {
    this();
    setAccessTokenValiditySeconds(prototype.getAccessTokenValiditySeconds());
    setRefreshTokenValiditySeconds(prototype.getRefreshTokenValiditySeconds());
    setAuthorities(prototype.getAuthorities());
    setAuthorizedGrantTypes(prototype.getAuthorizedGrantTypes());
    setClientId(prototype.getClientId());
    setClientSecret(prototype.getClientSecret());
    setRegisteredRedirectUri(prototype.getRegisteredRedirectUri());
    setScope(prototype.getScope());
    setResourceIds(prototype.getResourceIds());
  }

  public Oauth2Client(
      String clientId, String resourceIds, String scopes, String grantTypes, String authorities) {
    this(clientId, resourceIds, scopes, grantTypes, authorities, null);
  }

  public Oauth2Client(
      String clientId,
      String resourceIds,
      String scopes,
      String grantTypes,
      String authorities,
      String redirectUris) {

    this.clientId = clientId;

    if (StringUtils.hasText(resourceIds)) {
      Set<String> resources = StringUtils.commaDelimitedListToSet(resourceIds);
      if (!resources.isEmpty()) {
        this.resourceIds = resources;
      }
    }

    if (StringUtils.hasText(scopes)) {
      Set<String> scopeList = StringUtils.commaDelimitedListToSet(scopes);
      if (!scopeList.isEmpty()) {
        this.scope = scopeList;
      }
    }

    if (StringUtils.hasText(grantTypes)) {
      this.authorizedGrantTypes = StringUtils.commaDelimitedListToSet(grantTypes);
    } else {
      this.authorizedGrantTypes =
          new HashSet<String>(Arrays.asList("authorization_code", "refresh_token"));
    }

    if (StringUtils.hasText(authorities)) {
      this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    if (StringUtils.hasText(redirectUris)) {
      this.registeredRedirectUris = StringUtils.commaDelimitedListToSet(redirectUris);
    }
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  @Override
  public boolean isAutoApprove(String scope) {
    if (autoApproveScopes == null) {
      return false;
    }
    for (String auto : autoApproveScopes) {
      if (auto.equals("true") || scope.matches(auto)) {
        return true;
      }
    }
    return false;
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Set<String> getAutoApproveScopes() {
    return autoApproveScopes;
  }

  public void setAutoApproveScopes(Collection<String> autoApproveScopes) {
    this.autoApproveScopes = new HashSet<String>(autoApproveScopes);
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public boolean isSecretRequired() {
    return this.clientSecret != null;
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public boolean isScoped() {
    return this.scope != null && !this.scope.isEmpty();
  }

  public Set<String> getScope() {
    return scope;
  }

  public void setScope(Collection<String> scope) {
    this.scope = scope == null ? Collections.<String>emptySet() : new LinkedHashSet<String>(scope);
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Set<String> getResourceIds() {
    return resourceIds;
  }

  public void setResourceIds(Collection<String> resourceIds) {
    this.resourceIds =
        resourceIds == null
            ? Collections.<String>emptySet()
            : new LinkedHashSet<String>(resourceIds);
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Set<String> getAuthorizedGrantTypes() {
    return authorizedGrantTypes;
  }

  public void setAuthorizedGrantTypes(Collection<String> authorizedGrantTypes) {
    this.authorizedGrantTypes = new LinkedHashSet<String>(authorizedGrantTypes);
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Set<String> getRegisteredRedirectUri() {
    return registeredRedirectUris;
  }

  public void setRegisteredRedirectUri(Set<String> registeredRedirectUris) {
    this.registeredRedirectUris =
        registeredRedirectUris == null ? null : new LinkedHashSet<String>(registeredRedirectUris);
  }

  @org.codehaus.jackson.annotate.JsonProperty("authorities")
  @com.fasterxml.jackson.annotation.JsonProperty("authorities")
  private List<String> getAuthoritiesAsStrings() {
    return new ArrayList<String>(AuthorityUtils.authorityListToSet(authorities));
  }

  @org.codehaus.jackson.annotate.JsonProperty("authorities")
  @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
  @com.fasterxml.jackson.annotation.JsonProperty("authorities")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(
    using = Jackson2ArrayOrStringDeserializer.class
  )
  private void setAuthoritiesAsStrings(Set<String> values) {
    setAuthorities(AuthorityUtils.createAuthorityList(values.toArray(new String[values.size()])));
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities = new ArrayList<GrantedAuthority>(authorities);
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Integer getAccessTokenValiditySeconds() {
    return accessTokenValiditySeconds;
  }

  public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
    this.accessTokenValiditySeconds = accessTokenValiditySeconds;
  }

  @org.codehaus.jackson.annotate.JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public Integer getRefreshTokenValiditySeconds() {
    return refreshTokenValiditySeconds;
  }

  public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
    this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
  }

  @org.codehaus.jackson.annotate.JsonAnyGetter
  @com.fasterxml.jackson.annotation.JsonAnyGetter
  public Map<String, Object> getAdditionalInformation() {
    return Collections.unmodifiableMap(this.additionalInformation);
  }

  public void setAdditionalInformation(Map<String, ?> additionalInformation) {
    this.additionalInformation = new LinkedHashMap<String, Object>(additionalInformation);
  }

  @org.codehaus.jackson.annotate.JsonAnySetter
  @com.fasterxml.jackson.annotation.JsonAnySetter
  public void addAdditionalInformation(String key, Object value) {
    this.additionalInformation.put(key, value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        clientId,
        clientSecret,
        scope,
        resourceIds,
        authorizedGrantTypes,
        registeredRedirectUris,
        autoApproveScopes,
        authorities,
        accessTokenValiditySeconds,
        refreshTokenValiditySeconds,
        additionalInformation);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Oauth2Client that = (Oauth2Client) o;
    return Objects.equals(clientId, that.clientId)
        && Objects.equals(clientSecret, that.clientSecret)
        && Objects.equals(scope, that.scope)
        && Objects.equals(resourceIds, that.resourceIds)
        && Objects.equals(authorizedGrantTypes, that.authorizedGrantTypes)
        && Objects.equals(registeredRedirectUris, that.registeredRedirectUris)
        && Objects.equals(autoApproveScopes, that.autoApproveScopes)
        && Objects.equals(authorities, that.authorities)
        && Objects.equals(accessTokenValiditySeconds, that.accessTokenValiditySeconds)
        && Objects.equals(refreshTokenValiditySeconds, that.refreshTokenValiditySeconds)
        && Objects.equals(additionalInformation, that.additionalInformation);
  }

  public static class Builder {
    private final String clientId;

    private Collection<String> authorizedGrantTypes = new LinkedHashSet<>();

    private Collection<String> authorities = new LinkedHashSet<>();

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private Collection<String> scopes = new LinkedHashSet<>();

    private Collection<String> autoApproveScopes = new HashSet<>();

    private String secret;

    private Set<String> registeredRedirectUris = new HashSet<>();

    private Set<String> resourceIds = new HashSet<>();

    private boolean autoApprove;

    private Map<String, Object> additionalInformation = new LinkedHashMap<>();

    public Builder(String clientId) {
      this.clientId = clientId;
    }

    public ClientDetails build() {
      BaseClientDetails result = new BaseClientDetails();
      result.setClientId(clientId);
      result.setAuthorizedGrantTypes(authorizedGrantTypes);
      result.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
      result.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
      result.setRegisteredRedirectUri(registeredRedirectUris);
      result.setClientSecret(secret);
      result.setScope(scopes);
      result.setAuthorities(
          AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()])));
      result.setResourceIds(resourceIds);
      result.setAdditionalInformation(additionalInformation);
      if (autoApprove) {
        result.setAutoApproveScopes(scopes);
      } else {
        result.setAutoApproveScopes(autoApproveScopes);
      }
      return result;
    }

    public Builder resourceIds(String... resourceIds) {
      this.resourceIds.addAll(Arrays.asList(resourceIds));
      return this;
    }

    public Builder redirectUris(String... registeredRedirectUris) {
      this.registeredRedirectUris.addAll(Arrays.asList(registeredRedirectUris));
      return this;
    }

    public Builder authorizedGrantTypes(String... authorizedGrantTypes) {
      this.authorizedGrantTypes.addAll(Arrays.asList(authorizedGrantTypes));
      return this;
    }

    public Builder accessTokenValiditySeconds(int accessTokenValiditySeconds) {
      this.accessTokenValiditySeconds = accessTokenValiditySeconds;
      return this;
    }

    public Builder refreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
      this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
      return this;
    }

    public Builder secret(String secret) {
      this.secret = secret;
      return this;
    }

    public Builder scopes(String... scopes) {
      this.scopes.addAll(Arrays.asList(scopes));
      return this;
    }

    public Builder authorities(String... authorities) {
      this.authorities.addAll(Arrays.asList(authorities));
      return this;
    }

    public Builder autoApprove(boolean autoApprove) {
      this.autoApprove = autoApprove;
      return this;
    }

    public Builder autoApprove(String... scopes) {
      this.autoApproveScopes.addAll(Arrays.asList(scopes));
      return this;
    }

    public Builder additionalInformation(Map<String, ?> map) {
      this.additionalInformation.putAll(map);
      return this;
    }

    public Builder additionalInformation(String... pairs) {
      for (String pair : pairs) {
        String separator = ":";
        if (!pair.contains(separator) && pair.contains("=")) {
          separator = "=";
        }
        int index = pair.indexOf(separator);
        String key = pair.substring(0, index > 0 ? index : pair.length());
        String value = index > 0 ? pair.substring(index + 1) : null;
        this.additionalInformation.put(key, value);
      }
      return this;
    }

    public Builder and() {
      return Builder.this;
    }
  }
}
