package com.yoloo.server.user.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.yoloo.server.common.appengine.util.AppengineEnv
import com.yoloo.server.common.util.FcmConstants
import com.yoloo.server.common.vo.PubSubResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.apache.logging.log4j.LogManager
import java.io.IOException

class PostCreatedEventUseCase(
    private val objectMapper: ObjectMapper,
    private val firebaseMessaging: FirebaseMessaging,
    private val eventFilter: NanoCuckooFilter
) {

    fun execute(response: PubSubResponse) {
        val message = response.message
        val messageId = message.messageId

        if (eventFilter.contains(messageId)) {
            LOGGER.info("PostCreatedEvent - id: {} already consumed", messageId)
            return
        }

        val jsonData = message.jsonData
        LOGGER.info("Json data: {}", jsonData)

        try {
            val postResponse = objectMapper.readValue(jsonData, PostResponse::class.java)
            LOGGER.info("Post Response: {}", postResponse)

            val authorId = postResponse.author.id.toLong()

            val user = ofy().load().type(User::class.java).id(authorId).now()
            user.profile.countData.postCount = user.profile.countData.postCount.inc()

            ofy().save().entity(user)

            sendNewPostNotification(postResponse)
        } catch (e: IOException) {
            LOGGER.error("Couldn't parse response", e)
        }

        eventFilter.insert(messageId)
    }

    private fun sendNewPostNotification(response: PostResponse) {
        val message = Message.builder()
            .putData(FcmConstants.FCM_KEY_TYPE, FcmConstants.FcmType.FCM_TYPE_NEW_POST.toString())
            .putData("FCM_KEY_POST_ID", response.id)
            .setTopic("topic_${response.id}")
            .build()

        val dryRun = !AppengineEnv.isProd()
        firebaseMessaging.sendAsync(message, dryRun)
    }

    private data class PostResponse(val id: String, val author: Author) {

        data class Author(val id: String)
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}