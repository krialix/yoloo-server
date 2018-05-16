package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.*
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Component

@Component
class GetUserUseCase(private val memcacheService: MemcacheService) : UseCase<GetUserUseCase.Params, UserResponse> {

    override fun execute(params: Params): UserResponse {
        val requesterId = params.jwtClaims.sub
        val targetId = params.targetId

        var user = ofy().load().type(User::class.java).id(targetId).now()

        ServiceExceptions.checkBadRequest(user != null, "userId is invalid")

        val self = targetId == requesterId

        user = user.copy(
            self = self,
            following = when {
                self -> false
                else -> checkFollowing(requesterId, targetId)
            }
        )

        return mapToUserResponse(user)
    }

    private fun checkFollowing(requesterId: Long, targetId: Long): Boolean {
        return getRelationshipFilter().contains("$requesterId:$targetId")
    }

    private fun getRelationshipFilter(): NanoCuckooFilter {
        return memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter
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

    class Params(val jwtClaims: JwtClaims, val targetId: Long)
}