package com.yoloo.server.user.domain.requestpayload

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.validation.constraints.Conditional
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.format.annotation.NumberFormat
import javax.validation.constraints.*

@Conditional(selected = "providerType", values = ["yoloo"], required = ["password"])
@NoArg
data class InsertUserPayload(
    @field:NotBlank
    val clientId: String?,

    @field:NumberFormat
    @field:UniqueElements
    @field:NotNull
    @field:NotEmpty
    var subscribedGroupIds: List<Long>?,

    @field:UniqueElements
    var followedUserIds: List<Long>?,

    val providerIdToken: String?,

    @field:Pattern(regexp = "(yoloo|google|facebook)", message = "must match with yoloo, google or facebook")
    @field:NotNull
    val providerType: String?,

    @field:NotBlank
    val displayName: String?,

    @field:NotBlank
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