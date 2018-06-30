package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.format.annotation.NumberFormat
import javax.validation.Valid
import javax.validation.constraints.*
import javax.validation.constraints.Email

@NoArg
class UserCreateRequest(
    @field:NotBlank
    val displayName: String?,

    @field:NotBlank
    @field:Email
    val email: String?,

    @field:NotEmpty
    val photoUrl: String? = DEFAULT_IMAGE,

    val password: String?,

    @field:Pattern(regexp = "(google|facebook|email)", message = "must match with either google, facebook or email")
    @field:NotBlank
    val providerId: String?,

    val providerUid: String?,

    @field:NotBlank
    val clientId: String?,

    @field:NumberFormat
    @field:UniqueElements
    @field:NotNull
    @field:NotEmpty
    val subscribedGroupIds: List<Long>?,

    @field:UniqueElements
    val followedUserIds: List<Long>?,

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
) {
    companion object {
        const val DEFAULT_IMAGE = ""
    }
}