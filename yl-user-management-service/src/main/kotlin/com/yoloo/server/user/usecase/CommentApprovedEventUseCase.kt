package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.Message
import com.yoloo.server.common.appengine.service.NotificationService
import com.yoloo.server.common.util.FcmConstants
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.logging.log4j.LogManager
import java.io.IOException

class CommentApprovedEventUseCase(
    private val objectMapper: ObjectMapper,
    private val notificationService: NotificationService,
    private val eventFilter: NanoCuckooFilter
) {

    fun execute(response: PubSubResponse) {
        val message = response.message
        val messageId = message.messageId

        if (eventFilter.contains(messageId)) {
            LOGGER.info("CommentApprovedEvent - id: {} already consumed", messageId)
            return
        }

        val jsonData = message.jsonData
        LOGGER.info("Json data: {}", jsonData)

        try {
            val commentResponse = objectMapper.readValue(jsonData, CommentResponse::class.java)
            LOGGER.info("Comment Response: {}", commentResponse)

            val commentAuthorId = commentResponse.author.id.toLong()

            val user = ofy().load().type(User::class.java).id(commentAuthorId).now()

            sendCommentApprovedNotification(commentResponse.postId.value, user.fcmToken)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }

        eventFilter.insert(messageId)
    }

    private fun sendCommentApprovedNotification(postId: String, commentAuthorFcmToken: String) {
        val message = Message.builder()
            .putData(FcmConstants.FCM_KEY_TYPE, FcmConstants.FcmType.FCM_TYPE_COMMENT_APPROVE.toString())
            .putData("FCM_KEY_POST_ID", postId)
            .setToken(commentAuthorFcmToken)
            .build()

        notificationService.sendAsync(message)
    }

    private data class CommentResponse(val postId: PostId, val author: Author) {
        data class Author(val id: String)
        data class PostId(val value: String, val authorId: String)
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}