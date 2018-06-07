package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import org.apache.logging.log4j.LogManager
import java.io.IOException

// TODO Implement correct logic
class PostDeletedEventUseCase(private val objectMapper: ObjectMapper) {

    fun execute(response: PubSubResponse) {
        val message = response.message
        val messageId = message.messageId

        if (!CONSUMED_MESSAGE_IDS.contains(messageId)) {


            CONSUMED_MESSAGE_IDS.add(messageId)
        }
    }

    companion object {
        private val LOGGER = LogManager.getLogger()

        private val CONSUMED_MESSAGE_IDS = mutableSetOf<String>()
    }
}