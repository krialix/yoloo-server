package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.SearchUserResponse
import org.springframework.stereotype.Component

@Component
class SearchUserResponseMapper : Mapper<User, SearchUserResponse> {

    override fun apply(user: User): SearchUserResponse {
        return SearchUserResponse(id = user.id, displayName = user.displayName.value, avatarUrl = user.image.value)
    }
}