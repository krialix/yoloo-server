package com.yoloo.server.user.infrastructure.eventlistener

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.util.Filters
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.DisplayName
import com.yoloo.server.user.infrastructure.event.RelationshipEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class RelationshipEventListener(
    @Qualifier("relationship-queue") private val queue: Queue,
    private val memcacheService: AsyncMemcacheService,
    private val objectMapper: ObjectMapper
) : ApplicationListener<RelationshipEvent> {

    override fun onApplicationEvent(event: RelationshipEvent) {
        updateRelationshipCache(event)
        addToQueue(event)
    }

    private fun addToQueue(event: RelationshipEvent) {
        val payload = Payload(
            id = event.eventId,
            userId = event.userId,
            displayName = event.displayName,
            avatarImage = event.avatarImage,
            idFcmTokenMap = event.idFcmTokenMap
        )

        val json = objectMapper.writeValueAsString(payload)

        val taskOptions = TaskOptions.Builder
            .withMethod(TaskOptions.Method.PULL)
            .payload(json)

        queue.addAsync(taskOptions)
    }

    private fun updateRelationshipCache(event: RelationshipEvent) {
        val filter = memcacheService.get(Filters.KEY_FILTER_RELATIONSHIPS).get() as NanoCuckooFilter

        val userId = event.userId

        event.idFcmTokenMap
            .keys
            .map { "$userId:$it" }
            .forEach { filter.insert(it) }

        memcacheService.put(Filters.KEY_FILTER_RELATIONSHIPS, filter)
        TODO("implement notification")
    }

    internal data class Payload(
        val id: Long,
        val userId: Long,
        val displayName: DisplayName,
        val avatarImage: AvatarImage,
        val idFcmTokenMap: Map<Long, String>
    )
}