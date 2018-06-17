package com.yoloo.server.common.event

import com.google.firebase.messaging.Message
import org.springframework.context.ApplicationEvent

class FcmEvent(val message: Message, source: Any) : ApplicationEvent(source)