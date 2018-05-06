package com.yoloo.server.relationship.infrastructure.queue.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.relationship.infrastructure.queue.pull.RelationshipPullServlet
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfiguration {

    @Bean("relationship-queue")
    fun relationshipQueue(): Queue {
        return QueueFactory.getQueue("relationship-queue")
    }

    @Bean
    fun relationshipPullServlet(
        @Qualifier("relationship-queue") queue: Queue,
        objectMapper: ObjectMapper,
        @Qualifier("cached") idGenerator: LongIdGenerator
    ): ServletRegistrationBean<RelationshipPullServlet> {
        val bean = ServletRegistrationBean(
            RelationshipPullServlet(queue, objectMapper, idGenerator),
            "/tasks/pull/relationship"
        )
        bean.setLoadOnStartup(2)
        return bean
    }
}