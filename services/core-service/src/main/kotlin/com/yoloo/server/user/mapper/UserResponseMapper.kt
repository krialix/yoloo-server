package com.yoloo.server.user.mapper

import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.*

class UserResponseMapper {

    fun apply(from: User, self: Boolean, following: Boolean): UserResponse {
        return UserResponse(
            id = from.id,
            profileUrl = from.profile.profileUrl?.value,
            displayName = from.profile.displayName.value,
            about = from.profile.about?.value,
            profileImageUrl = from.profile.profileImageUrl.value,
            website = from.profile.websiteUrl?.value,
            locale = LocaleResponse(from.profile.locale.language, from.profile.locale.country),
            email = from.email.value,
            count = UserCountResponse(
                from.profile.countData.postCount,
                from.profile.countData.followerCount,
                from.profile.countData.followingCount
            ),
            self = self,
            following = following,
            subscribedGroups = from.subscribedGroups
                .map {
                    UserGroupResponse(
                        it.id,
                        it.displayName,
                        it.imageUrl
                    )
                },
            spokenLanguages = from.profile.spokenLanguages.map { LanguageResponse(it.value) }
        )
    }
}