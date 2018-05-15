package com.yoloo.server.user.infrastructure.social.provider;

import com.yoloo.server.user.infrastructure.social.UserInfo;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface UserInfoProvider {

  @Nonnull
  UserInfo getUserInfo(String token);
}
