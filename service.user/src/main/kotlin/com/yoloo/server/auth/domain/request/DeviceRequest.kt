package com.yoloo.server.auth.domain.request

import com.yoloo.server.common.util.NoArg
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@NoArg
data class DeviceRequest(
    @field:NotBlank
    val brand: String?,

    @field:NotBlank
    val model: String?,

    @field:Valid
    @field:NotNull
    val screen: Screen?,

    @field:Valid
    @field:NotNull
    val os: Os?,

    @field:NotBlank
    val localIp: String?
) {

    @NoArg
    data class Screen(
        @field:Positive
        @field:NotNull
        val dpi: Int?,

        @field:Positive
        @field:NotNull
        val width: Int?,

        @field:Positive
        @field:NotNull
        val height: Int?
    )

    @NoArg
    data class Os(
        @field:NotNull
        val type: String?,

        @field:NotNull
        val version: String?
    )
}