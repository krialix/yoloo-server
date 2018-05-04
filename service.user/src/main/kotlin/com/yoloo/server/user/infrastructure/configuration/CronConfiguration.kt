package com.yoloo.server.user.infrastructure.configuration

import com.google.appengine.api.urlfetch.URLFetchService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class CronConfiguration(private val urlFetchService: URLFetchService) {

    @Profile("dev")
    @Scheduled(cron = "*/10 * * * * *")
    fun leaseRelationshipPullQueue() {
        log.info("Running 'leaseRelationshipPullQueue' cron")
    }

    companion object {
        private val log = LoggerFactory.getLogger(CronConfiguration::class.java)
    }
}