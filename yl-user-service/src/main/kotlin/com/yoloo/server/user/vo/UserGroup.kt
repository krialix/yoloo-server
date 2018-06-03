package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Keyable

@NoArg
data class UserGroup(var id: Long, var imageUrl: String, var displayName: String) :
    Keyable<UserGroup>