package com.yoloo.server.user.domain.request

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.infrastructure.social.ProviderType
import javax.validation.constraints.Email

@NoArg
data class InsertUserRequest(
    val clientId: String,

    val subscribedGroupIds: List<String>,

    val token: String?,

    val providerType: ProviderType,

    val displayName: String?,

    @field:Email
    val email: String?,

    val password: String?,

    val locale: String,

    val gender: String,

    val lastKnownIP: String,

    val fcmToken: String
)