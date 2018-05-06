package com.yoloo.server.relationship.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.util.ServiceExceptions.checkNotFound
import com.yoloo.server.relationship.infrastructure.event.UnfollowEvent
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

        val cacheMap = memcacheService.getAll(
            listOf(Filters.KEY_FILTER_EMAIL, Filters.KEY_FILTER_RELATIONSHIP)
        ) as Map<String, *>

        val userFilter = cacheMap[Filters.KEY_FILTER_EMAIL] as NanoCuckooFilter
        val relationshipFilter = cacheMap[Filters.KEY_FILTER_RELATIONSHIP] as NanoCuckooFilter

        checkNotFound(userFilter.contains(toId) || !userFilter.contains("d:$toId"), "user.error.not-found")
        checkNotFound(relationshipFilter.contains("$fromId:$toId"), "relationship.error.not-found")

        publishUnfollowEvent(fromId, toId)
    }

    private fun publishUnfollowEvent(fromUserId: Long, toUserId: Long) {
        val event = UnfollowEvent(this, fromUserId, toUserId, objectMapper)

        eventPublisher.publishEvent(event)
    }

    class Request(val principal: Principal?, val userId: Long)
}