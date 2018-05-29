package com.yoloo.server.user.fetcher

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.user.vo.UserGroup
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL

interface GroupInfoFetcher {

    @Throws(IOException::class)
    fun fetch(ids: Collection<Long>): List<UserGroup>

    @Lazy
    @Profile("!dev")
    @Component
    class DefaultFetcher(
        private val urlFetchService: URLFetchService,
        private val objectMapper: ObjectMapper
    ) : GroupInfoFetcher {
        override fun fetch(ids: Collection<Long>): List<UserGroup> {
            try {
                val response = urlFetchService.fetch(URL(""))
                return objectMapper.readValue(
                    response.content,
                    object : TypeReference<List<UserGroup>>() {
                    })
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return emptyList()
        }
    }

    @Lazy
    @Profile("dev")
    @Component
    class StubFetcher : GroupInfoFetcher {
        override fun fetch(ids: Collection<Long>): List<UserGroup> {
            return ids.asSequence().map { UserGroup(it, "image", "name") }.toList()
        }
    }
}