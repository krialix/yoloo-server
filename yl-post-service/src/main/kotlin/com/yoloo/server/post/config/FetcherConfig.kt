package com.yoloo.server.post.config

import com.yoloo.server.post.fetcher.GroupInfoFetcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.web.client.RestTemplate

@Configuration
class FetcherConfig {

    @Lazy
    @Bean
    fun groupInfoFetcher(template: RestTemplate): GroupInfoFetcher {
        return GroupInfoFetcher.DefaultFetcher(template)
    }
}