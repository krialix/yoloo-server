package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@NoArg
data class CheckEmailAvailabilityRequest(
    @field:NotBlank
    @field:Email
    val email: String?
)