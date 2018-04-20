package com.yoloo.server.user.domain.usecase.impl

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.contract.GetUserUseCaseContract
import com.yoloo.server.user.infrastructure.mapper.UserResponseMapper
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.http.auth.BasicUserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetUserUseCaseImpl @Autowired constructor(
    private val userResponseMapper: UserResponseMapper,
    private val memcacheService: MemcacheService
) : GetUserUseCase {

    override fun execute(request: GetUserUseCaseContract.Request): GetUserUseCaseContract.Response {
        val userId = request.userId
        val userKey = Key.create(User::class.java, userId)
        val user = ofy().load().key(userKey).now()

        if (user == null || !user.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        val requesterId = request.principal?.name ?: BasicUserPrincipal("d9a37d25-422a-11e8-b84e-f108b6a390b8").name

        val cacheIds = listOf(
            "counter_follower:$userId",
            "counter_following:$userId",
            "filter_follower:$userId"
        )

        val values = memcacheService.getAll(cacheIds)

        val followerCount = values["counter_follower:$userId"] as Long? ?: user.countData.followerCount
        val followingCount = values["counter_following:$userId"] as Long? ?: user.countData.followingCount
        val followerCuckooFilter =
            values["filter_follower:$userId"] as NanoCuckooFilter? ?: user.userFilterData.followersFilter

        val data = userResponseMapper.apply(
            user, mutableMapOf(
                "followerCount" to followerCount,
                "followingCount" to followingCount,
                "following" to followerCuckooFilter.contains(requesterId),
                "self" to (requesterId == userId)
            )
        )

        return GetUserUseCaseContract.Response(data)
    }
}