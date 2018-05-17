package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.OnLoad
import com.yoloo.server.common.shared.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.vo.Email
import com.yoloo.server.user.vo.Profile
import com.yoloo.server.user.vo.UserGroup

@Cache(expirationSeconds = User.CACHE_EXPIRATION_TIME)
@NoArg
@Entity
data class User(
    @Id var id: Long,

    var email: Email,

    var profile: Profile,

    var subscribedGroups: List<UserGroup>,

    // Extra fields for easy mapping
    val self: Boolean = false,

    val following: Boolean = false
) : BaseEntity<User>(1) {

    @OnLoad
    fun onLoad() {
        @Suppress("USELESS_ELVIS")
        profile.spokenLanguages = profile.spokenLanguages ?: emptyList()
        @Suppress("USELESS_ELVIS")
        subscribedGroups = subscribedGroups ?: emptyList()
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 7200
    }
}