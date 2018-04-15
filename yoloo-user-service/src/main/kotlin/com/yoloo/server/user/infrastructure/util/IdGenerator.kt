package com.yoloo.server.user.infrastructure.util

import com.fasterxml.uuid.Generators

object IdGenerator {

    fun timestampUUID(): String {
        return Generators.timeBasedGenerator().generate().toString().replace("-", "")
    }

    fun timestampUUID(suffix: String): String {
        return timestampUUID(suffix, null)
    }

    fun timestampUUID(suffix: String, value: String?): String {
        return timestampUUID().plus(":$suffix:$value")
    }
}