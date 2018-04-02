package com.yoloo.server.user.domain.response

import org.springframework.hateoas.ResourceSupport
import java.time.LocalDateTime

data class UserResponse(
    val id: String,
    val displayName: String,
    val avatarUrl: String,
    val email: String,
    val createdAt: LocalDateTime,
    val bio: String?,
    val website: String?
) : ResourceSupport()