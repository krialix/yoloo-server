package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.LocaleResponse
import com.yoloo.server.user.domain.response.UserCountResponse
import com.yoloo.server.user.domain.response.UserGroupResponse
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class UserResponseMapper : Function<User, UserResponse> {

    override fun apply(from: User): UserResponse {
        return UserResponse(
            id = from.key.toWebSafeString(),
            url = from.url?.value,
            displayName = from.displayName.value,
            self = from.self,
            following = from.following,
            about = from.about?.value,
            avatarUrl = from.image.value,
            email = from.email.value,
            website = from.website?.value,
            count = UserCountResponse(from.countData.postCount, from.followerCount, from.followingCount),
            locale = LocaleResponse(from.locale.language, from.locale.country),
            subscribedGroups = from.subscribedGroups.map { UserGroupResponse(it.id, it.displayName, it.imageUrl) }
        )
    }
}