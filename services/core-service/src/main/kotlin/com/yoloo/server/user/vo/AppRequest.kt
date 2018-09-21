package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@NoArg
data class AppRequest(
    @field:NotBlank
    val googleAdvertisingId: String?,

    @field:NotBlank
    val version: String?,

    @field:NotNull
    val debug: Boolean?
)