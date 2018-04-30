package com.yoloo.server.user.domain.requestpayload

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.validation.constraints.Conditional
import com.yoloo.server.common.validation.constraints.NullOrNotBlank
import org.hibernate.validator.constraints.UniqueElements
import javax.validation.constraints.*

@Conditional(selected = "providerType", values = ["yoloo"], required = ["displayName", "email", "password"])
@NoArg
data class InsertUserPayload(
    @field:NotBlank
    val clientId: String?,

    @field:UniqueElements
    @field:NotNull
    @field:NotEmpty
    var subscribedGroupIds: List<String>?,

    @field:UniqueElements
    var followedUserIds: List<String>?,

    val token: String?,

    @field:Pattern(regexp = "(yoloo|google|facebook)", message = "must match with yoloo, google or facebook")
    @field:NotNull
    val providerType: String?,

    @field:NullOrNotBlank
    val displayName: String?,

    @field:Email
    val email: String?,

    val password: String?,

    @field:NotBlank
    val locale: String?,

    @field:Pattern(regexp = "(male|female)", message = "must match with either male or female")
    @field:NotNull
    val gender: String?,

    @field:NotBlank
    val lastKnownIP: String?,

    @field:NotBlank
    val fcmToken: String?
)