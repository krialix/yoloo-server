package com.yoloo.server.user.infrastructure.event.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.DisplayName
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class GroupSubscriptionEventListener(
    @Qualifier("subscription-queue") private val queue: Queue,
    private val memcacheService: AsyncMemcacheService,
    private val objectMapper: ObjectMapper
) : ApplicationListener<GroupSubscriptionEvent> {

    override fun onApplicationEvent(event: GroupSubscriptionEvent) {
        updateGroupSubscriptionCache(event)
        addToQueue(event)
    }

    private fun addToQueue(event: GroupSubscriptionEvent) {
        val payload = Payload(
            id = event.eventId,
            userId = event.userId,
            displayName = event.displayName,
            avatarImage = event.avatarImage,
            groupIds = event.groupIds
        )

        val json = objectMapper.writeValueAsString(payload)

        val taskOptions = TaskOptions.Builder
            .withMethod(TaskOptions.Method.PULL)
            .payload(json)

        queue.addAsync(taskOptions)
    }

    private fun updateGroupSubscriptionCache(event: GroupSubscriptionEvent) {
        val filterIds = event.groupIds.map { "f_subscription:$it" }

        val map = memcacheService.getAll(filterIds).get() as MutableMap<String, NanoCuckooFilter>

        val userId = event.userId
        map.forEach { _, v -> v.insert(userId) }

        memcacheService.putAll(map)
    }

    internal data class Payload(
        val id: Long,
        val userId: Long,
        val displayName: DisplayName,
        val avatarImage: AvatarImage,
        val groupIds: List<Long>
    )
}