package com.yoloo.server.user.infrastructure.event

import org.springframework.context.ApplicationEvent

class GroupSubscriptionEvent(source: Any, val groupIds: List<String>) : ApplicationEvent(source) {

    constructor(source: Any, groupId: String) : this(source, listOf(groupId))
}
