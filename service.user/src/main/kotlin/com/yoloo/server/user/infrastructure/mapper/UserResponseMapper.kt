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
            count = UserCountResponse(
                posts = from.countData.postCount,
                followers = from.followerCount,
                followings = from.followingCount
            ),
            locale = LocaleResponse(language = from.locale.language, country = from.locale.country),
            subscribedGroups = from.subscribedGroups
                .map { UserGroupResponse(id = it.id, displayName = it.displayName, imageUrl = it.imageUrl) }
        )
    }
}