package com.yoloo.server.user.event.listener

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.user.event.RefreshFeedEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class RefreshFeedEventListener(
    /*@Qualifier("refresh-feed-queue") private val queue: Queue*/
) : ApplicationListener<RefreshFeedEvent> {

    override fun onApplicationEvent(event: RefreshFeedEvent) {
        val taskOptions = TaskOptions.Builder
            .withMethod(TaskOptions.Method.PULL)
            .payload(event.groupIds.joinToString(","))
        //queue.addAsync(taskOptions)
    }
}