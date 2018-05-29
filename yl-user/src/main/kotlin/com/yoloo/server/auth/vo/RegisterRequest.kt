package com.yoloo.server.auth.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.validation.constraints.Conditional
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.format.annotation.NumberFormat
import javax.validation.Valid
import javax.validation.constraints.*

@Conditional.List(
    value =
    [
        Conditional(selected = "providerId", values = ["yoloo"], required = ["email", "password"]),
        Conditional(
            selected = "providerId",
            values = ["google", "facebook"],
            required = ["providerId"]
        ),
        Conditional(selected = "providerId", values = ["google"], required = ["googleIdToken"]),
        Conditional(
            selected = "providerId",
            values = ["facebook"],
            required = ["facebookAccessToken"]
        )
    ]
)
@NoArg
data class RegisterRequest(
    val providerId: String?,

    val federatedId: String?,

    val googleIdToken: String?,

    val facebookAccessToken: String?,

    @field:NotBlank
    val displayName: String?,

    @field:NotBlank
    @field:Email
    val email: String?,

    @field:NotBlank
    val password: String?,

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

    @Valid
    @field:NotNull
    val device: DeviceRequest?,

    @Valid
    @field:NotNull
    val app: AppRequest?,

    @field:NotBlank
    val country: String?,

    @field:NotBlank
    val language: String?
)