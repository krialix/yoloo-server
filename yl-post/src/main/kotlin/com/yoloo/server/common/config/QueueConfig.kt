package com.yoloo.server.common.config

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfig {

    @Bean("counter-queue")
    fun subscriptionQueue(): Queue {
        return QueueFactory.getQueue("counter-queue")
    }
}