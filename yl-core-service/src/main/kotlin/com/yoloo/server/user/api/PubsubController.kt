package com.yoloo.server.user.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.user.usecase.*
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/_ah/pubsub")
class PubsubController(
    private val objectMapper: ObjectMapper,
    private val postCreatedEventUseCase: PostCreatedEventUseCase,
    private val postDeletedEventUseCase: PostDeletedEventUseCase,
    private val commentCreatedEventUseCase: CommentCreatedEventUseCase,
    private val commentDeletedEventUseCase: CommentDeletedEventUseCase,
    private val commendApprovedEventUseCase: CommentApprovedEventUseCase
) {

    @PostMapping("/post.delete")
    fun postDeletedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = PubSubResponse.from(objectMapper, request.inputStream)
            postDeletedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    @PostMapping("/comment.create")
    fun commentCreatedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = PubSubResponse.from(objectMapper, request.inputStream)
            commentCreatedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    @PostMapping("/comment.delete")
    fun commentDeletedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = PubSubResponse.from(objectMapper, request.inputStream)
            commentDeletedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    @PostMapping("/comment.approved")
    fun commentApprovedEvent(request: HttpServletRequest) {
        try {
            val pubSubResponse = PubSubResponse.from(objectMapper, request.inputStream)
            commendApprovedEventUseCase.execute(pubSubResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}