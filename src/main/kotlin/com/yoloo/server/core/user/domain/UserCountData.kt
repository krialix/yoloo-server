package com.yoloo.server.core.user.domain

import com.yoloo.server.core.util.NoArg

@NoArg
data class UserCountData(val postCount: Int = 0, val commentCount: Int = 0, val coinCount: Int = 0)