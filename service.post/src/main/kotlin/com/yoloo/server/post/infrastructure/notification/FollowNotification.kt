package com.yoloo.server.post.infrastructure.notification

import com.google.api.core.ApiFuture
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.yoloo.server.common.FcmNotification

class FollowNotification(
    private val firebaseMessaging: FirebaseMessaging,
    private val followerId: String,
    private val followerDisplayName: String,
    private val registrationToken: String
) : FcmNotification {

    override fun sendAsync(): ApiFuture<String> {
        val message = Message.builder()
            .putData(FCM_KEY_TYPE, "follow")
            .putData(FCM_KEY_USER_ID, followerId)
            .putData(FCM_KEY_USER_DISPLAYNAME, followerDisplayName)
            .setToken(registrationToken)
            .build()

        return firebaseMessaging.sendAsync(message)
    }

    companion object {
        private const val FCM_KEY_TYPE = "FCM_TYPE"
        private const val FCM_KEY_USER_ID = "FCM_USER_ID"
        private const val FCM_KEY_USER_DISPLAYNAME = "FCM_USER_DISPLAYNAME"
    }
}