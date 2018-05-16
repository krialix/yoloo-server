package com.yoloo.server.user.social.provider.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.yoloo.server.user.social.UserInfo
import com.yoloo.server.user.social.provider.UserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class GoogleUserInfoProvider(private val verifier: GoogleIdTokenVerifier) : UserInfoProvider {

    override fun getUserInfo(token: String): UserInfo {
        val googlePayload = verifier.verify(token).payload

        return UserInfo(
            providerId = googlePayload.subject,
            picture = googlePayload["picture"] as String
        )
    }
}