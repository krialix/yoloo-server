package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.util.AppengineEnv
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.exception.ServiceExceptions
import com.yoloo.server.rest.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter

// TODO needs test
class UnfollowUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, userId: Long) {
        val map = ofy().load().type(User::class.java).ids(requesterId, userId)
        val fromUser = map[requesterId]
        val toUser = map[userId]

        ServiceExceptions.checkNotFound(toUser != null, "user.not_found")

        val relationshipFilter = getRelationshipFilter()

        checkNotFound(
            Relationship.isFollowing(relationshipFilter, requesterId, userId),
            "relationship.not_found"
        )

        fromUser!!.profile.countData.followingCount = fromUser.profile.countData.followingCount--
        toUser!!.profile.countData.followerCount = fromUser.profile.countData.followerCount--

        val saveFuture = ofy().save().entities(fromUser, toUser)
        val relationshipKey = Relationship.createKey(requesterId, userId)
        val deleteFuture = ofy().delete().key(relationshipKey)
        relationshipFilter.delete(relationshipKey.name)

        val putFuture = memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, relationshipFilter)

        if (AppengineEnv.isTest()) {
            saveFuture.now()
            deleteFuture.now()
            putFuture.get()
        }
    }

    private fun getRelationshipFilter(): NanoCuckooFilter {
        return memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP).get() as NanoCuckooFilter
    }
}