package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.logging.log4j.LogManager
import java.io.IOException

class CommentDeletedEventUseCase(private val objectMapper: ObjectMapper, private val eventFilter: NanoCuckooFilter) {

    fun execute(response: PubSubResponse) {
        val message = response.message
        val messageId = message.messageId

        if (eventFilter.contains(messageId)) {
            LOGGER.info("CommentDeletedEvent - id: {} already consumed", messageId)
            return
        }

        val jsonData = message.jsonData
        LOGGER.info("Json data: {}", jsonData)

        try {
            val commentResponse = objectMapper.readValue(jsonData, CommentResponse::class.java)
            LOGGER.info("Comment Response: {}", commentResponse)

            val authorId = commentResponse.author.id.toLong()

            val user = ofy().load().type(User::class.java).id(authorId).now()
            user.profile.countData.commentCount = user.profile.countData.commentCount.dec()

            ofy().save().entity(user)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }

        eventFilter.insert(messageId)
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    private data class CommentResponse(val author: Author) {

        data class Author(val id: String)
    }
}