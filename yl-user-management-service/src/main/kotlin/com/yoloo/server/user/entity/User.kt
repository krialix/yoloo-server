package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.vo.*

@Cache(expirationSeconds = User.CACHE_EXPIRATION_TIME)
@NoArg
@Entity
data class User(
    @Id var id: Long,

    var clientId: String,

    //var provider: Provider,

    @Index
    var email: Email,

    //var emailVerified: Boolean = false,

    var accountVerified: Boolean = false,

    //var expired: Boolean = false,

    //var credentialsExpired: Boolean = false,

    //var locked: Boolean = false,

    //var disabled: Boolean = false,

    var roles: Set<@JvmSuppressWildcards Role>,

    //var lastSignInTime: LocalDateTime? = null,

    var fcmToken: String,

    var profile: Profile,

    var subscribedGroups: List<UserGroup>,

    var appInfo: AppInfo,

    var device: Device
) : BaseEntity<User>() {

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        profile.spokenLanguages = profile.spokenLanguages ?: emptyList()
        @Suppress("USELESS_ELVIS")
        subscribedGroups = subscribedGroups ?: emptyList()
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 7200

        const val KEY_FILTER_USER_IDENTIFIER = "FILTER_IDENTIFIER"

        const val INDEX_EMAIL = "email.value"
    }

    enum class Role {
        ANONYMOUS,
        MEMBER,
        ADMIN
    }
}