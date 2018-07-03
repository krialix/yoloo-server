package com.yoloo.server.common.event

import org.springframework.context.ApplicationEvent

class PubSubEvent(val topic: String, val payload: Any, source: Any) : ApplicationEvent(source)