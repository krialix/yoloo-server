package com.yoloo.server.user.infrastructure.eventlistener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Profile("dev")
@Component
class StartupApplicationListener : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        warmUpCache()
    }

    private fun warmUpCache() {
        RestTemplate().postForLocation("http://localhost:8081/api/v1/admin/cache/warmup", null)
    }

    private fun createOauth2Clients() {

    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StartupApplicationListener::class.java)
    }
}