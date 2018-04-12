package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.FollowingResponse
import org.springframework.stereotype.Component

@Component
class FollowingResponseMapper : Mapper<User, FollowingResponse> {

    override fun apply(user: User): FollowingResponse {
        return FollowingResponse(id = user.id, displayName = user.displayName.value, avatarUrl = user.avatarUrl)
    }
}