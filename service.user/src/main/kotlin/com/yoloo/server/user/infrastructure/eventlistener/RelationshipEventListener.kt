package com.yoloo.server.user.infrastructure.eventlistener

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.util.Filters
import com.yoloo.server.relationship.infrastructure.event.FollowEvent
import com.yoloo.server.relationship.infrastructure.event.RelationshipEvent
import com.yoloo.server.relationship.infrastructure.event.UnfollowEvent
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

        if (event is FollowEvent) {
            filter.insert("${event.fromUserId}:${event.toUserId}")
        } else if (event is UnfollowEvent) {
            filter.delete("${event.fromUserId}:${event.toUserId}")
        }

        memcacheService.put(Filters.KEY_FILTER_RELATIONSHIP, filter)
    }

    private fun getRelationshipCuckooFilter(): NanoCuckooFilter {
        return memcacheService.get(Filters.KEY_FILTER_RELATIONSHIP).get() as NanoCuckooFilter?
                ?: NanoCuckooFilter.Builder(32).build()
    }
}