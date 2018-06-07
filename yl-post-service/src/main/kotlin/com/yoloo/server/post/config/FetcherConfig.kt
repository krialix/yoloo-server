package com.yoloo.server.post.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.post.fetcher.DefaultGroupInfoFetcher
import com.yoloo.server.post.fetcher.DefaultUserInfoFetcher
import com.yoloo.server.post.fetcher.StubGroupInfoFetcher
import com.yoloo.server.post.fetcher.StubUserInfoFetcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile

@Configuration
class FetcherConfig {

    @Profile("!dev")
    @Lazy
    @Bean
    fun defaultGroupInfoFetcher(urlFetchService: URLFetchService, objectMapper: ObjectMapper): DefaultGroupInfoFetcher {
        return DefaultGroupInfoFetcher(urlFetchService, objectMapper)
    }

    @Lazy
    @Bean
    fun stubGroupInfoFetcher(): StubGroupInfoFetcher {
        return StubGroupInfoFetcher()
    }

    @Profile("!dev")
    @Lazy
    @Bean
    fun defaultUserInfoFetcher(urlFetchService: URLFetchService, objectMapper: ObjectMapper): DefaultUserInfoFetcher {
        return DefaultUserInfoFetcher()
    }

    @Lazy
    @Bean
    fun stubUserInfoFetcher(): StubUserInfoFetcher {
        return StubUserInfoFetcher()
    }
}