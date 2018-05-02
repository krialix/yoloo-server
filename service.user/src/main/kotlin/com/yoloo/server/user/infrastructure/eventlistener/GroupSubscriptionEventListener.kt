package com.yoloo.server.user.infrastructure.eventlistener

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.cache.CacheService
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class GroupSubscriptionEventListener(
    @Qualifier("counter-queue") private val queue: Queue,
    private val cacheService: CacheService
) : ApplicationListener<GroupSubscriptionEvent> {

    override fun onApplicationEvent(event: GroupSubscriptionEvent) {
        updateGroupSubscriptionCache(event)
        addToQueue(event.groupIds)
    }

    private fun addToQueue(groupIds: List<Long>) {
        val taskOptions = TaskOptions.Builder
            .withMethod(TaskOptions.Method.PULL)
            .payload(groupIds.joinToString(","))

        queue.addAsync(taskOptions)
    }

    private fun updateGroupSubscriptionCache(event: GroupSubscriptionEvent) {
        val groupSubscriptionFilterIds = event.groupIds.map { "filter_subscription:$it" }

        val map = cacheService.getAll(groupSubscriptionFilterIds) as MutableMap<String, NanoCuckooFilter>

        val userId = event.userInfo.userId

        map.forEach { _, v -> v.insert(userId) }

        cacheService.putAllAsync(map)
    }
}