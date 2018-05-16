package com.yoloo.server.user.vo

import javax.validation.constraints.Email

data class PatchUserRequest(
    @field:Email
    var email: String?,

    var displayName: String?
)