package com.yoloo.server.user.infrastructure.userinfo

import java.util.function.Supplier

interface UserInfoProvider: Supplier<UserInfo>