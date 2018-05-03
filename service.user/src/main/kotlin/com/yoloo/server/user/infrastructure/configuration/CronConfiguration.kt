package com.yoloo.server.user.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class CronConfiguration {

    @Scheduled(cron = "*/10 * * * * *")
    fun leasePullQueue() {
        println("Running cron")
    }
}