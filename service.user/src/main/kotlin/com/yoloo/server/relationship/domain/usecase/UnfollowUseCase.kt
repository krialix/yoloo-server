package com.yoloo.server.relationship.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkNotFound
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.infrastructure.event.RelationshipEvent
import com.yoloo.server.user.domain.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class UnfollowUseCase(
    private val memcacheService: MemcacheService,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper
) : UseCase<UnfollowUseCase.Request, Unit> {

    override fun execute(request: Request) {
        val fromId = request.principal!!.name.toLong()
        val toId = request.userId

        val map = ofy().load().type(User::class.java).ids(fromId, toId)
        val fromUser = map[fromId]
        val toUser = map[toId]

        User.checkUserExistsAndEnabled(fromUser)
        User.checkUserExistsAndEnabled(toUser)

        val relationshipFilter = memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP) as NanoCuckooFilter

        checkNotFound(relationshipFilter.contains("$fromId:$toId"), "relationship.error.not-found")

        publishUnfollowEvent(fromId, toId)
    }

    private fun publishUnfollowEvent(fromUserId: Long, toUserId: Long) {
        val event = RelationshipEvent.Unfollow(this, fromUserId, toUserId, objectMapper)
        eventPublisher.publishEvent(event)
    }

    class Request(val principal: Principal?, val userId: Long)
}