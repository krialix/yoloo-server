package com.yoloo.server.user.infrastructure.social.provider;

import com.yoloo.server.user.infrastructure.social.UserInfo;
import com.yoloo.server.user.infrastructure.social.RequestPayload;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface UserInfoProvider {

  @Nonnull
  UserInfo getUserInfo(@Nonnull RequestPayload payload);
}
