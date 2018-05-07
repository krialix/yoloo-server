package com.yoloo.server.user.infrastructure.eventlistener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@DependsOn("ofy")
@Profile("dev")
@Component
class StartupApplicationListener: ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {

    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StartupApplicationListener::class.java)
    }
}