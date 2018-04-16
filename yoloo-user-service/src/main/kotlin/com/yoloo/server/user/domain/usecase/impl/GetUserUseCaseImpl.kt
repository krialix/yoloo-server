package com.yoloo.server.user.domain.usecase.impl

import com.google.appengine.api.memcache.MemcacheService
import com.googlecode.objectify.Key
import com.yoloo.server.common.Mapper
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.entity.UserGroupBucket
import com.yoloo.server.user.domain.response.UserResponse
import com.yoloo.server.user.domain.usecase.GetUserUseCase
import com.yoloo.server.user.domain.usecase.contract.GetUserUseCaseContract
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetUserUseCaseImpl @Autowired constructor(
    private val userResponseMapper: Mapper<User, UserResponse>,
    private val memcacheService: MemcacheService
) : GetUserUseCase {

    override fun execute(request: GetUserUseCaseContract.Request): GetUserUseCaseContract.Response {
        val userId = request.userId
        val userKey = Key.create(User::class.java, userId)
        val subscribedGroupBucketKey = UserGroupBucket.createNamespaceKey(userId)
        val map = ofy().load().keys(userKey, subscribedGroupBucketKey) as Map<*, *>

        val user = map[userKey] as User?
        val userGroupBucket = map[subscribedGroupBucketKey] as UserGroupBucket

        if (user == null || !user.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        val requesterId = request.principal.name

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

        user.countData.followerCount = followerCount
        user.countData.followingCount = followingCount
        user.following = followerCuckooFilter.contains(requesterId)
        user.self = requesterId == userId

        val response = userResponseMapper.apply(user)
        // TODO implement user group mapper
        userGroupBucket.userGroups

        val data = JsonApi.data(response).withIncludedResources()

        return GetUserUseCaseContract.Response(data)
    }
}