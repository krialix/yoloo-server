package com.yoloo.server.user.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.*
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url
import com.yoloo.server.entity.Keyable
import com.yoloo.server.entity.Relationable
import com.yoloo.server.user.vo.*
import java.time.Instant

@Cache(expirationSeconds = User.CACHE_TTL)
@NoArg
@Entity
data class User(
    @Id var id: Long,

    var hashId: String,

    @Index
    var email: Email,

    var accountVerified: Boolean = false,

    var roles: Set<@JvmSuppressWildcards Role>,

    var fcmToken: String,

    var profile: Profile,

    var subscribedGroups: List<UserGroup>,

    var appInfo: AppInfo,

    var device: Device,

    var createdAt: Instant = Instant.now(),

    var deletedAt: Instant? = null
) : Keyable<User>, Relationable {

    override fun getRelationableId(): Long {
        return id
    }

    override fun getRelationableDisplayName(): DisplayName {
        return profile.displayName
    }

    override fun getRelationableProfileImageUrl(): Url {
        return profile.profileImageUrl
    }

    override fun incFollowerCount() {
    }

    override fun incFollowingCount() {
    }

    override fun decFollowerCount() {
    }

    override fun decFollowingCount() {
    }

    @OnLoad
    fun onLoad() {
        @Suppress("USELESS_ELVIS")
        profile.spokenLanguages = profile.spokenLanguages ?: emptyList()
        @Suppress("USELESS_ELVIS")
        subscribedGroups = subscribedGroups ?: emptyList()
    }

    companion object {
        const val CACHE_TTL = 7200

        const val KEY_FILTER_USER_IDENTIFIER = "FILTER_IDENTIFIER"

        const val INDEX_EMAIL = "email.email"

        fun createKey(userId: Long): Key<User> {
            return Key.create(User::class.java, userId)
        }
    }

    enum class Role {
        ANONYMOUS,
        MEMBER,
        ADMIN
    }
}
