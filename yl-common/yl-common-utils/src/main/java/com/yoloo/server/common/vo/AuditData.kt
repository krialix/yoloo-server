package com.yoloo.server.common.vo

import java.time.LocalDateTime

data class AuditData(
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null
) {

    val isDeleted: Boolean
        get() = deletedAt != null
}
