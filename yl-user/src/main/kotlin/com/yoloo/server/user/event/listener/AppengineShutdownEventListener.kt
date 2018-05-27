package com.yoloo.server.user.event.listener

import com.yoloo.server.common.event.AppengineShutdownEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class AppengineShutdownEventListener : ApplicationListener<AppengineShutdownEvent> {

    override fun onApplicationEvent(event: AppengineShutdownEvent) {
        LOGGER.info("--- APPENGINE SHUTTING DOWN ---")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AppengineShutdownEventListener::class.java)
    }
}