package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.error.exception.ServiceExceptions
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class GetUserUseCase(private val memcacheService: MemcacheService) {

    fun execute(requesterId: Long, targetId: Long): UserResponse {
        val user = ofy().load().type(User::class.java).id(targetId).now()

        ServiceExceptions.checkBadRequest(user != null, "userId is invalid")

        val self = targetId == requesterId

        user.self = self
        user.following = when {
            self -> false
            else -> Relationship.isFollowing(getRelationshipFilter(), requesterId, targetId)
        }

        return mapToUserResponse(user)
    }

    private fun getRelationshipFilter(): NanoCuckooFilter {
        return memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter
    }

    private fun mapToUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            profileUrl = user.profile.profileUrl?.value,
            displayName = user.profile.displayName.value,
            about = user.profile.about?.value,
            avatarUrl = user.profile.image.url.value,
            website = user.profile.websiteUrl?.value,
            locale = LocaleResponse(user.profile.locale.language, user.profile.locale.country),
            email = user.email.value,
            count = UserCountResponse(
                user.profile.countData.postCount,
                user.profile.countData.followerCount,
                user.profile.countData.followingCount
            ),
            self = user.self,
            following = user.following,
            subscribedGroups = user.subscribedGroups.map {
                UserGroupResponse(
                    it.id,
                    it.displayName,
                    it.imageUrl
                )
            },
            spokenLanguages = user.profile.spokenLanguages.map { LanguageResponse(it.value) }
        )
    }
}