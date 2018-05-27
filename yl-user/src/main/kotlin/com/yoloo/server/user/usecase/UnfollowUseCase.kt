package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.rest.error.exception.ServiceExceptions.checkNotFound
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.event.RelationshipEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class UnfollowUseCase(
    private val memcacheService: MemcacheService,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper
) {

    fun execute(requesterId: Long, userId: Long) {
        val relationshipFilter = memcacheService.get(Relationship.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter

        checkNotFound(Relationship.isFollowing(relationshipFilter, requesterId, userId), "relationship.not_found")

        publishUnfollowEvent(requesterId, userId)
    }

    private fun publishUnfollowEvent(fromUserId: Long, toUserId: Long) {
        val event = RelationshipEvent.Unfollow(this, fromUserId, toUserId, objectMapper)
        eventPublisher.publishEvent(event)
    }
}