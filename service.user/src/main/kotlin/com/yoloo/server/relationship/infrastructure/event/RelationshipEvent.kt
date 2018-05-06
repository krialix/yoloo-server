package com.yoloo.server.relationship.infrastructure.event

import org.springframework.context.ApplicationEvent

abstract class RelationshipEvent(source: Any) : ApplicationEvent(source) {

    abstract fun toByteArray(): ByteArray

    abstract fun tag(): String
}