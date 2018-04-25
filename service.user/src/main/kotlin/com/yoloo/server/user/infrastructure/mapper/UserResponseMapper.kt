package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.util.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.LocaleResponse
import com.yoloo.server.user.domain.response.UserCountResponse
import com.yoloo.server.user.domain.response.UserGroupResponse
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.stereotype.Component

@Component
class UserResponseMapper : Mapper<User, UserResponse> {

    override fun apply(from: User, payload: Map<String, Any>): UserResponse {
        return UserResponse(
            id = from.key.toWebSafeString(),
            url = from.url?.value,
            displayName = from.displayName.value,
            self = payload["self"] as Boolean,
            following = payload["following"] as Boolean,
            about = from.about?.value,
            avatarUrl = from.image.value,
            email = from.email.value,
            website = from.website?.value,
            count = UserCountResponse(
                posts = from.countData.postCount,
                followers = payload["followerCount"] as Long,
                followings = payload["followingCount"] as Long
            ),
            locale = LocaleResponse(language = from.locale.language, country = from.locale.country),
            subscribedGroups = from.subscribedGroups
                .map { UserGroupResponse(id = it.id, displayName = it.displayName, imageUrl = it.imageUrl) }
        )
    }
}