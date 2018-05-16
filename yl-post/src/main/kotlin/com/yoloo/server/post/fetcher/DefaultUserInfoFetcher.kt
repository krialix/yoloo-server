package com.yoloo.server.post.fetcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.common.api.exception.BadRequestException
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.response.UserInfoResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL

@Profile("!dev")
@Component
class DefaultUserInfoFetcher(
    private val urlFetchService: URLFetchService,
    private val objectMapper: ObjectMapper
) : Fetcher<Long, UserInfoResponse> {

    override fun fetch(id: Long): UserInfoResponse {
        try {
            val response = urlFetchService.fetch(URL(""))
            return objectMapper.readValue(response.content, UserInfoResponse::class.java)
        } catch (e: IOException) {
            throw BadRequestException("100")
        }
    }
}