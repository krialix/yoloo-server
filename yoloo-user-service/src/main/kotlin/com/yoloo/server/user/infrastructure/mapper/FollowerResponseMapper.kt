package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.FollowerResponse
import org.springframework.stereotype.Component

@Component
class FollowerResponseMapper : Mapper<User, FollowerResponse> {

    override fun apply(user: User): FollowerResponse {
        return FollowerResponse(id = user.id, displayName = user.displayName.value, avatarUrl = user.avatarUrl)
    }
}