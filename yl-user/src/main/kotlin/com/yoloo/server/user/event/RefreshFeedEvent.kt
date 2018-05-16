package com.yoloo.server.user.event

import org.springframework.context.ApplicationEvent

class RefreshFeedEvent(source: Any, val groupIds: List<Long>) : ApplicationEvent(source)
