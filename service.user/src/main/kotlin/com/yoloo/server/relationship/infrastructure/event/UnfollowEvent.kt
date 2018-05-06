package com.yoloo.server.relationship.infrastructure.event

import com.fasterxml.jackson.databind.ObjectMapper

class UnfollowEvent(
    source: Any,
    val fromUserId: Long,
    val toUserId: Long,
    private val objectMapper: ObjectMapper
) : RelationshipEvent(source) {

    override fun tag(): String {
        return "unfollow"
    }

    override fun toByteArray(): ByteArray {
        return objectMapper.writeValueAsBytes(toPayload())
    }

    private fun toPayload(): Payload {
        return Payload(fromUserId = fromUserId, toUserId = toUserId)
    }

    data class Payload(val fromUserId: Long, val toUserId: Long)
}