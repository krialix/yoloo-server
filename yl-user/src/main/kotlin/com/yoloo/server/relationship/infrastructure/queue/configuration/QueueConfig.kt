package com.yoloo.server.relationship.infrastructure.queue.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import com.google.appengine.api.urlfetch.HTTPMethod
import com.google.appengine.api.urlfetch.HTTPRequest
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.relationship.infrastructure.queue.pull.RelationshipPullServlet
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.net.URL

@Configuration
class QueueConfig(private val urlFetchService: URLFetchService) {

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

    //@Profile("dev")
    //@Scheduled(fixedRate = 1000000)
    fun leaseRelationshipPullQueue() {
        log.info("Running 'leaseRelationshipPullQueue' cron")
        val url = "http://localhost:8081/tasks/pull/relationship"
        val httpResponse = urlFetchService.fetch(HTTPRequest(URL(url), HTTPMethod.POST))
        log.info("Response: {}", httpResponse.responseCode)
    }

    companion object {
        private val log = LoggerFactory.getLogger(QueueConfig::class.java)
    }
}