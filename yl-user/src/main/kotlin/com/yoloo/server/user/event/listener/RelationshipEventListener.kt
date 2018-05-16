package com.yoloo.server.user.event.listener

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.util.Filters
import com.yoloo.server.user.event.RelationshipEvent
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class RelationshipEventListener(
    @Qualifier("relationship-queue") private val queue: Queue,
    private val memcacheService: AsyncMemcacheService
) : ApplicationListener<RelationshipEvent> {

    override fun onApplicationEvent(event: RelationshipEvent) {
        updateRelationshipCache(event)
        addToQueue(event)
    }

    private fun addToQueue(event: RelationshipEvent) {
        val taskOptions = TaskOptions.Builder
            .withMethod(TaskOptions.Method.PULL)
            .taskName(event.tag())
            .payload(event.toByteArray())

        queue.addAsync(taskOptions)
    }

    private fun updateRelationshipCache(event: RelationshipEvent) {
        val filter = getRelationshipCuckooFilter()

        when (event) {
            is RelationshipEvent.Follow -> filter.insert("${event.fromUserId}:${event.toUserId}")
            is RelationshipEvent.Unfollow -> filter.delete("${event.fromUserId}:${event.toUserId}")
        }

        memcacheService.put(Filters.KEY_FILTER_RELATIONSHIP, filter)
    }

    private fun getRelationshipCuckooFilter(): NanoCuckooFilter {
        return memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP).get() as NanoCuckooFilter
    }
}