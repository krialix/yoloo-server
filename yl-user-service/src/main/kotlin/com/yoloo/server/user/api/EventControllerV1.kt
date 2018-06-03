package com.yoloo.server.user.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.user.usecase.PostCreatedEventUseCase
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/users/events")
class EventControllerV1(
    private val objectMapper: ObjectMapper,
    private val postCreatedEventUseCase: PostCreatedEventUseCase
) {

    @PostMapping("/post.create")
    fun postCreatedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = convertToPubSubResponse(request)
            postCreatedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    @Throws(IOException::class)
    private fun convertToPubSubResponse(request: HttpServletRequest): PubSubResponse {
        return objectMapper.readValue<PubSubResponse>(
            request.inputStream,
            PubSubResponse::class.java
        )
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventControllerV1::class.java)
    }
}