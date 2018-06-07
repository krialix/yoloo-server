package com.yoloo.server.user.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.user.usecase.PostCreatedEventUseCase
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/users/events")
class EventController(
    private val objectMapper: ObjectMapper,
    private val postCreatedEventUseCase: PostCreatedEventUseCase
) {

    @PostMapping("/post.create")
    fun postCreatedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = PubSubResponse.from(objectMapper, request.inputStream)
            postCreatedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    @PostMapping("/post.delete")
    fun postDeletedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = PubSubResponse.from(objectMapper, request.inputStream)
            postCreatedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}