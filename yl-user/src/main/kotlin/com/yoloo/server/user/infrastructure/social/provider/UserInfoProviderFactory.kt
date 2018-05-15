package com.yoloo.server.user.infrastructure.social.provider

import com.yoloo.server.auth.domain.vo.Provider
import com.yoloo.server.user.infrastructure.social.provider.facebook.FacebookUserInfoProvider
import com.yoloo.server.user.infrastructure.social.provider.google.GoogleUserInfoProvider
import com.yoloo.server.user.infrastructure.social.provider.yoloo.EmailUserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class UserInfoProviderFactory(
    private val facebookUserInfoProvider: FacebookUserInfoProvider,
    private val googleUserInfoProvider: GoogleUserInfoProvider,
    private val emailUserInfoProvider: EmailUserInfoProvider
) {

    fun create(type: Provider.Type): UserInfoProvider {
        return when (type) {
            Provider.Type.FACEBOOK -> facebookUserInfoProvider
            Provider.Type.GOOGLE -> googleUserInfoProvider
            Provider.Type.EMAIL -> emailUserInfoProvider
        }
    }
}