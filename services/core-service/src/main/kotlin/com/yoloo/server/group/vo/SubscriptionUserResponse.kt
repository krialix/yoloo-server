package com.yoloo.server.group.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class SubscriptionUserResponse(var id: Long, var profileImageUrl: String, var displayName: String)