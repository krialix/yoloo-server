package com.yoloo.server.user.infrastructure.social.provider.yoloo

import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class YolooUserInfoProvider : UserInfoProvider {

    override fun getUserInfo(token: String): UserInfo {
        return UserInfo(providerId = null, providerType = ProviderType.YOLOO, picture = DEFAULT_USER_IMAGE)
    }

    companion object {
        private const val DEFAULT_USER_IMAGE = ""
    }
}