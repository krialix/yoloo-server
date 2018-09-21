package com.yoloo.server.common.vo

import java.time.Instant

data class AuditData(
    var createdAt: Instant = Instant.now(),
    var deletedAt: Instant? = null
) {

    val isDeleted: Boolean
        get() = deletedAt != null
}
