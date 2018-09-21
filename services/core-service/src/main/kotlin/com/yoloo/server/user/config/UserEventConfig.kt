package com.yoloo.server.user.config

import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class UserEventConfig {

    @Lazy
    @Bean(EVENT_FILTER)
    fun eventFilter(): NanoCuckooFilter {
        return NanoCuckooFilter.Builder(32L).build()
    }

    companion object {
        const val EVENT_FILTER = "core-event-filter"
    }
}