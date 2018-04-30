package com.yoloo.server.user.infrastructure.eventlistener

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.user.infrastructure.event.GroupSubscriptionEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class GroupSubscriptionEventListener(
    @Qualifier("counter-queue") private val queue: Queue
) : ApplicationListener<GroupSubscriptionEvent> {

    override fun onApplicationEvent(event: GroupSubscriptionEvent) {
        val taskOptions = TaskOptions.Builder
            .withMethod(TaskOptions.Method.PULL)
            .payload(event.groupIds.joinToString(","))
        queue.addAsync(taskOptions)
    }
}