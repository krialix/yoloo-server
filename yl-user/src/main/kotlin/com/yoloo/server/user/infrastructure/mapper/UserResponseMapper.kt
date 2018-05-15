package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.*
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class UserResponseMapper : Function<User, UserResponse> {

    override fun apply(from: User): UserResponse {
        return UserResponse(
            id = from.id,
            profileUrl = from.profile.profileUrl?.value,
            displayName = from.profile.displayName.value,
            about = from.profile.about?.value,
            avatarUrl = from.profile.image.url.value,
            website = from.profile.websiteUrl?.value,
            locale = LocaleResponse(from.profile.locale.language, from.profile.locale.country),
            email = from.email.value,
            count = UserCountResponse(
                from.profile.countData.postCount,
                from.profile.countData.followerCount,
                from.profile.countData.followingCount
            ),
            self = from.self,
            following = from.following,
            subscribedGroups = from.subscribedGroups.map { UserGroupResponse(it.id, it.displayName, it.imageUrl) },
            spokenLanguages = from.profile.spokenLanguages.map { LanguageResponse(it.value) }
        )
    }
}