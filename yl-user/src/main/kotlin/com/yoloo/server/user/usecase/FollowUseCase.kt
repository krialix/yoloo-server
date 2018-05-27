package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.error.exception.ServiceExceptions.checkConflict
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.event.RelationshipEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class FollowUseCase(
    private val memcacheService: MemcacheService,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper
) {

    fun execute(requesterId: Long, userId: Long) {
        val map = ofy().load().type(User::class.java).ids(requesterId, userId)
        val fromUser = map[requesterId]
        val toUser = map[userId]

        val relationshipFilter = memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter

        checkConflict(!Relationship.isFollowing(relationshipFilter, requesterId, userId), "relationship.conflict")

        publishFollowEvent(fromUser!!, toUser!!)
    }

    private fun publishFollowEvent(fromUser: User, toUser: User) {
        val event = RelationshipEvent.Follow(
            this,
            fromUser.id,
            fromUser.profile.displayName,
            fromUser.profile.image,
            toUser.id,
            "",
            objectMapper
        )
        eventPublisher.publishEvent(event)
    }
}