package com.yoloo.server.relationship.infrastructure.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.DisplayName

class FollowEvent(
    source: Any,
    val fromUserId: Long,
    val fromDisplayName: DisplayName,
    val fromAvatarImage: AvatarImage,
    val toUserId: Long,
    val toUserFcmToken: String,
    private val objectMapper: ObjectMapper
) : RelationshipEvent(source) {

    override fun tag(): String {
        return "follow"
    }

    override fun toByteArray(): ByteArray {
        return objectMapper.writeValueAsBytes(toPayload())
    }

    private fun toPayload(): Payload {
        return Payload(
            fromUserId = fromUserId,
            fromDisplayName = fromDisplayName,
            fromAvatarImage = fromAvatarImage,
            toUserId = toUserId,
            toUserFcmToken = toUserFcmToken
        )
    }

    data class Payload(
        val fromUserId: Long,
        val fromDisplayName: DisplayName,
        val fromAvatarImage: AvatarImage,
        val toUserId: Long,
        val toUserFcmToken: String
    )
}