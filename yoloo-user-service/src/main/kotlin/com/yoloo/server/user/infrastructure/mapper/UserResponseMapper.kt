package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserCountResponse
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.stereotype.Component

@Component
class UserResponseMapper : Mapper<User, UserResponse> {

    override fun apply(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            displayName = user.displayName.value,
            self = user.self,
            following = user.following,
            bio = user.bio,
            avatarUrl = user.avatarUrl,
            email = user.email.value,
            website = user.website,
            count = UserCountResponse(followers = user.followerCount, followings = user.followingCount),
            countryIsoCode = user.locale.language
        )
    }
}