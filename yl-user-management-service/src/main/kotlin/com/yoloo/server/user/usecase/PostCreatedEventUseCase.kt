package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import org.apache.logging.log4j.LogManager
import java.io.IOException

class PostCreatedEventUseCase(private val objectMapper: ObjectMapper) {

    fun execute(response: PubSubResponse) {
        val message = response.message
        val messageId = message.messageId

        if (!CONSUMED_MESSAGE_IDS.contains(messageId)) {
            val jsonData = message.jsonData
            LOGGER.info("Json data: {}", jsonData)

            try {
                val postResponse = objectMapper.readValue(jsonData, PostResponse::class.java)
                LOGGER.info("Post Response: {}", postResponse)

                val authorId = postResponse.author.id.toLong()

                val user = ofy().load().type(User::class.java).id(authorId).now()
                user.profile.countData.postCount = user.profile.countData.postCount++

                ofy().save().entity(user)
            } catch (e: IOException) {
                LOGGER.error("Couldn't parse response", e)
            }

            CONSUMED_MESSAGE_IDS.add(messageId)
        }
    }

    private data class PostResponse(val author: Author) {

        data class Author(val id: String)
    }

    companion object {
        private val LOGGER = LogManager.getLogger()

        private val CONSUMED_MESSAGE_IDS = mutableSetOf<String>()
    }
}