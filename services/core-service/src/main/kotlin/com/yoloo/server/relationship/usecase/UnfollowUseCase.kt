package com.yoloo.server.relationship.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.exception.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.stereotype.Service

@Service
class UnfollowUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(fromId: Long, toId: Long) {
        val map = ofy().load().type(User::class.java).ids(fromId, toId)
        val fromUser = map[fromId]
        val toUser = map[toId]

        ServiceExceptions.checkNotFound(toUser != null, "user.not_found")

        val relationshipFilter = memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP).get() as NanoCuckooFilter

        checkNotFound(
                Relationship.isFollowing(relationshipFilter, fromId, toId),
                "relationship.not_found"
        )

        fromUser!!.unfollow(toUser!!)

        ofy().save().entities(fromUser, toUser)

        val relationshipKey = Relationship.createKey(fromId, toId)
        ofy().delete().key(relationshipKey)

        relationshipFilter.delete(relationshipKey.name)
        memcacheService.put(Relationship.KEY_FILTER_RELATIONSHIP, relationshipFilter)
    }
}
