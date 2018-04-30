package com.yoloo.server.user.infrastructure.event

import org.springframework.context.ApplicationEvent

class UserRelationshipEvent(source: Any, fcmTokens: List<String>) : ApplicationEvent(source) {

    constructor(source: Any, fcmToken: String): this(source, listOf(fcmToken))
}