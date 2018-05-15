package com.yoloo.server.user.infrastructure.social.provider.yoloo

import com.yoloo.server.user.infrastructure.social.UserInfo
import com.yoloo.server.user.infrastructure.social.provider.UserInfoProvider
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class EmailUserInfoProvider : UserInfoProvider {

    override fun getUserInfo(token: String?): UserInfo {
        return UserInfo(providerId = null, picture = DEFAULT_USER_IMAGE)
    }

    companion object {
        private const val DEFAULT_USER_IMAGE = ""
    }
}