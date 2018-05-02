package com.yoloo.server.user.infrastructure.social.provider.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class GoogleUserInfoProvider(private val verifier: GoogleIdTokenVerifier) : UserInfoProvider {

    override fun getUserInfo(token: String): UserInfo {
        val googlePayload = verifier.verify(token).payload

        return UserInfo(
            providerId = googlePayload.subject,
            providerType = ProviderType.GOOGLE,
            picture = googlePayload["picture"] as String
        )
    }
}