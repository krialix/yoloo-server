package com.yoloo.server.user.infrastructure.social.provider.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.RequestPayload
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class GoogleUserInfoProvider(private val verifier: GoogleIdTokenVerifier) : UserInfoProvider {

    override fun getUserInfo(payload: RequestPayload): UserInfo {
        val googlePayload = verifier.verify(payload.token).payload

        return UserInfo(
            providerId = googlePayload.subject,
            providerType = ProviderType.GOOGLE,
            email = googlePayload.email,
            picture = googlePayload["picture"] as String,
            displayName = payload.displayName ?: googlePayload["name"] as String
        )
    }
}