package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.util.Filters
import com.yoloo.server.api.exception.ServiceExceptions.checkConflict
import com.yoloo.server.objectify.ObjectifyProxy.ofy
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

    fun execute(jwtClaims: JwtClaims, userId: Long) {
        val fromId = jwtClaims.sub

        val map = ofy().load().type(User::class.java).ids(fromId, userId)
        val fromUser = map[fromId]
        val toUser = map[userId]

        /*User.checkUserExistsAndEnabled(fromUser)
        User.checkUserExistsAndEnabled(toUser)*/

        val relationshipFilter = memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter

        checkConflict(!relationshipFilter.contains("$fromId:$userId"), "relationship.error.exists")

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