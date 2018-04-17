package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.LocaleResponse
import com.yoloo.server.user.domain.response.UserCountResponse
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.stereotype.Component

@Component
class UserResponseMapper(private val userGroupResponseMapper: UserGroupResponseMapper) : Mapper<User, UserResponse> {

    override fun apply(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            url = user.url?.value,
            displayName = user.displayName.value,
            self = user.self,
            following = user.following,
            about = user.about?.value,
            avatarUrl = user.image.value,
            email = user.email.value,
            website = user.website?.value,
            count = UserCountResponse(
                posts = user.countData.postCount,
                followers = user.countData.followerCount,
                followings = user.countData.followingCount
            ),
            locale = LocaleResponse(language = user.locale.language, country = user.locale.country),
            subscribedGroups = user.subscribedGroups.map(userGroupResponseMapper::apply)
        )
    }
}