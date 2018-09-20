package com.yoloo.server.user.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Url
import com.yoloo.server.relationship.vo.Relationable
import com.yoloo.server.user.vo.*

@Cache(expirationSeconds = User.CACHE_EXPIRATION_TIME)
@NoArg
@Entity
data class User(
        @Id var id: Long,

        var clientId: String,

        @Index
        var email: Email,

        var accountVerified: Boolean = false,

        var roles: Set<@JvmSuppressWildcards Role>,

        var fcmToken: String,

        var profile: Profile,

        var subscribedGroups: List<UserGroup>,

        var appInfo: AppInfo,

        var device: Device
) : BaseEntity<User>(), Relationable {

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
        profile.countData.followerCount++
    }

    override fun incFollowingCount() {
        profile.countData.followingCount++
    }

    override fun decFollowerCount() {
        profile.countData.followerCount--
    }

    override fun decFollowingCount() {
        profile.countData.followingCount--
    }

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
