package com.yoloo.server.post.config

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class QueueConfig {

    @Lazy
    @Bean(QUEUE_POST)
    fun postQueue(): Queue {
        return QueueFactory.getQueue(QUEUE_POST)
    }

    companion object {
        const val QUEUE_POST = "queue-post"
    }
}