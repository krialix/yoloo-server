package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.format.annotation.NumberFormat
import javax.validation.Valid
import javax.validation.constraints.*
import javax.validation.constraints.Email

@NoArg
abstract class UserRegisterRequest(
    @field:NotBlank
    open val displayName: String?,

    @field:NotBlank
    @field:Email
    open val email: String?,

    @field:NotEmpty
    val photoUrl: String? = DEFAULT_IMAGE,

    @field:NotBlank
    val password: String?,

    @field:Pattern(regexp = "(google|facebook|email)", message = "must match with either google, facebook or email")
    @field:NotBlank
    val providerId: String?,

    val providerUid: String?,

    @field:NotBlank
    open val clientId: String?,

    @field:NumberFormat
    @field:UniqueElements
    @field:NotNull
    @field:NotEmpty
    open val subscribedGroupIds: List<Long>?,

    @field:UniqueElements
    open val followedUserIds: List<Long>?,

    @field:Pattern(regexp = "(male|female)", message = "must match with either male or female")
    @field:NotNull
    open val gender: String?,

    @field:NotBlank
    open val fcmToken: String?,

    @field:Valid
    @field:NotNull
    open val device: DeviceRequest?,

    @field:Valid
    @field:NotNull
    open val app: AppRequest?,

    @field:NotBlank
    open val country: String?,

    @field:NotBlank
    open val language: String?
) {
    companion object {
        const val DEFAULT_IMAGE = ""
    }
}