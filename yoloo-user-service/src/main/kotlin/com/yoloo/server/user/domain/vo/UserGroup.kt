package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.util.NoArg

@NoArg
data class UserGroup(private var groupId: String) : Keyable<UserGroup>