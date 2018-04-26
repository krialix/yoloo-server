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

    @field:NotNull
    val providerType: ProviderType?,

    @field:NotBlank
    val displayName: String?,

    @field:Email
    val email: String?,

    val password: String?,

    @field:NotBlank
    val locale: String?,

    @field:NotBlank
    val gender: String?,

    @field:NotBlank
    val lastKnownIP: String?,

    @field:NotBlank
    val fcmToken: String?
)