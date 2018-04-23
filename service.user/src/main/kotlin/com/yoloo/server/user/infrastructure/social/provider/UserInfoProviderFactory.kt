package com.yoloo.server.user.infrastructure.social.provider

import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.provider.facebook.FacebookUserInfoProvider
import com.yoloo.server.user.infrastructure.social.provider.google.GoogleUserInfoProvider
import com.yoloo.server.user.infrastructure.social.provider.yoloo.YolooUserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class UserInfoProviderFactory(
    private val facebookUserInfoProvider: FacebookUserInfoProvider,
    private val googleUserInfoProvider: GoogleUserInfoProvider,
    private val yolooUserInfoProvider: YolooUserInfoProvider
) {

    fun create(type: ProviderType): UserInfoProvider {
        return when (type) {
            ProviderType.FACEBOOK -> facebookUserInfoProvider
            ProviderType.GOOGLE -> googleUserInfoProvider
            ProviderType.YOLOO -> yolooUserInfoProvider
        }
    }
}