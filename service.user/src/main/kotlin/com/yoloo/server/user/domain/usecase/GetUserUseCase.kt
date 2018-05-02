package com.yoloo.server.user.domain.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.http.auth.BasicUserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class GetUserUseCase @Autowired constructor(
    private val userResponseMapper: UserResponseMapper,
    private val memcacheService: MemcacheService
) : UseCase<GetUserUseCase.Request, UserResponse> {

    override fun execute(request: Request): UserResponse {
        val userId = request.userId
        val userKey = Key.create(User::class.java, userId)
        var user = ofy().load().key(userKey).now()

        if (user == null || !user.account.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        val requesterId = request.principal?.name ?: BasicUserPrincipal("d9a37d25-422a-11e8-b84e-f108b6a390b8").name

        val cacheIds = listOf("c_follower:$userId", "c_following:$userId", "f_follower:$userId")

        val values = memcacheService.getAll(cacheIds)

        val followerCount = values["c_follower:$userId"] as Long? ?: user.profile.countData.followerCount
        val followingCount = values["c_following:$userId"] as Long? ?: user.profile.countData.followingCount
        val followerCuckooFilter =
            values["f_follower:$userId"] as NanoCuckooFilter? ?: user.userFilterData.followersFilter

        user = user.copy(
            self = requesterId == userId,
            following = followerCuckooFilter.contains(requesterId),
            profile = user.profile.copy(
                countData = user.profile.countData.copy(
                    followerCount = followerCount,
                    followingCount = followingCount
                )
            )
        )

        return userResponseMapper.apply(user)
    }

    class Request(val principal: Principal?, val userId: String)
}