package com.yoloo.server.user.infrastructure.event

import org.springframework.context.ApplicationEvent

class RefreshFeedEvent(source: Any, val groupIds: List<Long>) : ApplicationEvent(source)
