package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkNotFound
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.event.RelationshipEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class UnfollowUseCase(
    private val memcacheService: MemcacheService,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper
) {

    fun execute(jwtClaims: JwtClaims, userId: Long) {
        val fromId = jwtClaims.sub

        val map = ofy().load().type(User::class.java).ids(fromId, userId)
        val fromUser = map[fromId]
        val toUser = map[userId]

        /*User.checkUserExistsAndEnabled(fromUser)
        User.checkUserExistsAndEnabled(toUser)*/

        val relationshipFilter = memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter

        checkNotFound(relationshipFilter.contains("$fromId:$userId"), "relationship.error.not_found")

        publishUnfollowEvent(fromId, userId)
    }

    private fun publishUnfollowEvent(fromUserId: Long, toUserId: Long) {
        val event = RelationshipEvent.Unfollow(this, fromUserId, toUserId, objectMapper)
        eventPublisher.publishEvent(event)
    }
}