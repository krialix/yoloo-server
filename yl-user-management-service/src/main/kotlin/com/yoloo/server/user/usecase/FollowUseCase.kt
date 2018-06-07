package com.yoloo.server.user.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.exception.ServiceExceptions
import com.yoloo.server.rest.exception.ServiceExceptions.checkConflict
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter

class FollowUseCase(private val memcacheService: MemcacheService) {

    fun execute(requesterId: Long, userId: Long) {
        val map = ofy().load().type(User::class.java).ids(requesterId, userId)
        val fromUser = map[requesterId]
        val toUser = map[userId]

        ServiceExceptions.checkNotFound(toUser != null, "user.not_found")

        val relationshipFilter = getRelationshipFilter()

        checkConflict(
            !Relationship.isFollowing(relationshipFilter, requesterId, userId),
            "relationship.conflict"
        )

        fromUser!!.profile.countData.followingCount = fromUser.profile.countData.followingCount++
        toUser!!.profile.countData.followerCount = fromUser.profile.countData.followerCount++

        val relationship = createRelationship(fromUser, toUser)

        ofy().save().entities(fromUser, toUser, relationship)
    }

    private fun getRelationshipFilter(): NanoCuckooFilter {
        return memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter
    }

    private fun createRelationship(fromUser: User, toUser: User): Relationship {
        return Relationship(
            id = Relationship.createId(fromUser.id, toUser.id),
            fromId = fromUser.id,
            toId = toUser.id,
            displayName = fromUser.profile.displayName,
            avatarImage = fromUser.profile.image
        )
    }
}