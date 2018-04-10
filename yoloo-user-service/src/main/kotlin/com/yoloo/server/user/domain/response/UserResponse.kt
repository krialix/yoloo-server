package com.yoloo.server.user.domain.response

import com.yoloo.server.common.util.NoArg
import java.time.LocalDateTime

@NoArg
data class UserResponse(
    var id: String,
    var displayName: String,
    var avatarUrl: String,
    var email: String,
    var createdAt: LocalDateTime,
    var bio: String?,
    var website: String?
)