package com.yoloo.server.user.social.provider;

import com.yoloo.server.user.social.UserInfo;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface UserInfoProvider {

  @Nonnull
  UserInfo getUserInfo(String token);
}
