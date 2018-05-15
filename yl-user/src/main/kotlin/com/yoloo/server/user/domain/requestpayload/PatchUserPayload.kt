package com.yoloo.server.user.domain.requestpayload

import javax.validation.constraints.Email

data class PatchUserPayload(
    @field:Email
    var email: String?,

    var displayName: String?
)