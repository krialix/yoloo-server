package com.yoloo.server.user.domain.request

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.infrastructure.social.ProviderType
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@NoArg
data class InsertUserRequest(
    @field:NotBlank
    val clientId: String?,

    @field:NotNull
    @field:NotEmpty
    var subscribedGroupIds: List<String>?,

    val token: String?,

    @field:NotNull(message = "user.register.empty.providerType")
    val providerType: ProviderType?,

    @field:NotBlank(message = "user.register.empty.displayName")
    val displayName: String?,

    @field:Email(message = "email.invalid")
    val email: String?,

    val password: String?,

    @field:NotBlank(message = "user.register.empty.locale")
    val locale: String?,

    @field:NotBlank(message = "user.register.empty.gender")
    val gender: String?,

    @field:NotBlank(message = "user.register.empty.lastKnownIP")
    val lastKnownIP: String?,

    @field:NotBlank(message = "user.register.empty.fcmToken")
    val fcmToken: String?
)