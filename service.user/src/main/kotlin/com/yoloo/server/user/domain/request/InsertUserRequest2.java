package com.yoloo.server.user.domain.request;

import com.yoloo.server.user.infrastructure.social.ProviderType;

import java.util.List;

public class InsertUserRequest2 {
  private final String clientId;
  private final List<String> subscribedGroupIds;
  private final String token;
  private final ProviderType providerType;
  private final String displayName;
  private final String email;
  private final String password;
  private final String locale;
  private final String gender;
  private final String lastKnownId;
  private final String fcmToken;

  public InsertUserRequest2(
      String clientId,
      List<String> subscribedGroupIds,
      String token,
      ProviderType providerType,
      String displayName,
      String email,
      String password,
      String locale,
      String gender,
      String lastKnownId,
      String fcmToken) {
    this.clientId = clientId;
    this.subscribedGroupIds = subscribedGroupIds;
    this.token = token;
    this.providerType = providerType;
    this.displayName = displayName;
    this.email = email;
    this.password = password;
    this.locale = locale;
    this.gender = gender;
    this.lastKnownId = lastKnownId;
    this.fcmToken = fcmToken;
  }

  public String getClientId() {
    return clientId;
  }

  public List<String> getSubscribedGroupIds() {
    return subscribedGroupIds;
  }

  public String getToken() {
    return token;
  }

  public ProviderType getProviderType() {
    return providerType;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getLocale() {
    return locale;
  }

  public String getGender() {
    return gender;
  }

  public String getLastKnownId() {
    return lastKnownId;
  }

  public String getFcmToken() {
    return fcmToken;
  }
}
