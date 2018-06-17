package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.format.annotation.NumberFormat
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@NoArg
data class RegisterFacebookRequest(
    @field:NotBlank
    val accessToken: String?,

    @field:NumberFormat
    @field:UniqueElements
    @field:NotNull
    @field:NotEmpty
    var subscribedGroupIds: List<Long>?,

    @field:UniqueElements
    var followedUserIds: List<Long>?,

    @field:Pattern(regexp = "(male|female)", message = "must match with either male or female")
    @field:NotNull
    val gender: String?,

    @field:NotBlank
    val fcmToken: String?,

    @field:Valid
    @field:NotNull
    val device: DeviceRequest?,

    @field:Valid
    @field:NotNull
    val app: AppRequest?,

    @field:NotBlank
    val country: String?,

    @field:NotBlank
    val language: String?
)