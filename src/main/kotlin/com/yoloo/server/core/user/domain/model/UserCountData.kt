package com.yoloo.server.core.user.domain.model

import com.yoloo.server.core.util.NoArg

@NoArg
data class UserCountData(var postCount: Int = 0, var commentCount: Int = 0, var coinCount: Int = 0)