package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class UserGroup(var id: Long, var imageUrl: String, var displayName: String)