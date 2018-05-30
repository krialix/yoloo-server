package com.yoloo.server.post.fetcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.urlfetch.URLFetchService
import com.yoloo.server.common.util.Fetcher
import com.yoloo.server.post.vo.GroupInfoResponse
import com.yoloo.server.rest.error.exception.BadRequestException
import java.io.IOException
import java.net.URL

class DefaultGroupInfoFetcher(
    private val urlFetchService: URLFetchService,
    private val objectMapper: ObjectMapper
) : Fetcher<Long, GroupInfoResponse> {

    override fun fetch(id: Long): GroupInfoResponse {
        try {
            val response = urlFetchService.fetch(URL(""))
            return objectMapper.readValue(response.content, GroupInfoResponse::class.java)
        } catch (e: IOException) {
            throw BadRequestException("101")
        }
    }
}