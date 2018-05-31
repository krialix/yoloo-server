package com.yoloo.server.group.vo

data class GroupResponse(
    val id: Long,
    val displayName: String,
    val description: String,
    val count: GroupCountResponse,
    val subscribed: Boolean
)