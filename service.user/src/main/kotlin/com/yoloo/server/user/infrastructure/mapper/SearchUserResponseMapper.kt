package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.util.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.SearchUserResponse
import org.springframework.stereotype.Component

@Component
class SearchUserResponseMapper : Mapper<User, SearchUserResponse> {

    override fun apply(from: User, payload: MutableMap<String, Any>): SearchUserResponse {
        return SearchUserResponse(
            id = from.key.toWebSafeString(),
            displayName = from.displayName.value,
            avatarUrl = from.image.value
        )
    }
}