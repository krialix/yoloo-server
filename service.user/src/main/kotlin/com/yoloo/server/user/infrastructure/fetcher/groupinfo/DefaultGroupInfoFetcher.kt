package com.yoloo.server.user.infrastructure.fetcher.groupinfo

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.user.domain.vo.UserGroup
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL

@Profile("!dev")
@Component
class DefaultGroupInfoFetcher(
    private val urlFetchService: URLFetchService,
    private val objectMapper: ObjectMapper
) : GroupInfoFetcher {

    override fun fetch(ids: Collection<Long>): List<UserGroup> {
        try {
            val response = urlFetchService.fetch(URL(""))
            return objectMapper.readValue(response.content, object : TypeReference<List<UserGroup>>() {

            })
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return emptyList()
    }
}
