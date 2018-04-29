package com.yoloo.server.user.domain.vo

import com.yoloo.server.common.shared.Keyable
import com.yoloo.server.common.util.NoArg

@NoArg
data class UserGroup(var id: String, var imageUrl: String, var displayName: String) :
    Keyable<UserGroup>