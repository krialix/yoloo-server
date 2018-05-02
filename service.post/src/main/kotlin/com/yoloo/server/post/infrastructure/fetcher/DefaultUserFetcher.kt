package com.yoloo.server.post.infrastructure.fetcher

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.domain.response.UserInfoResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL

@Profile("!dev")
@Component
class DefaultUserFetcher(
    private val urlFetchService: URLFetchService,
    private val objectMapper: ObjectMapper
) : Fetcher<UserInfoResponse> {

    override fun fetchAll(ids: Collection<Long>): Collection<UserInfoResponse> {
        try {
            val response = urlFetchService.fetch(URL(""))
            return objectMapper.readValue(response.content, object : TypeReference<List<UserInfoResponse>>() {

            })
        } catch (e: IOException) {
            e.printStackTrace()
        }


        return emptyList<UserInfoResponse>()
    }
}