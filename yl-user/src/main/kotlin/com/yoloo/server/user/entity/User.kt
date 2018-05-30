package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.vo.Email
import com.yoloo.server.user.vo.Profile
import com.yoloo.server.user.vo.UserGroup

@Cache(expirationSeconds = User.CACHE_EXPIRATION_TIME)
@NoArg
@Entity
class User(
    @Id
    var id: Long,

    var email: Email,

    var profile: Profile,

    var subscribedGroups: List<UserGroup>
) : BaseEntity<Long, User>() {

    override fun getId(): Long {
        return id
    }

    override fun sameIdentityAs(other: User?): Boolean {
        return equals(other)
    }

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        profile.spokenLanguages = profile.spokenLanguages ?: emptyList()
        @Suppress("USELESS_ELVIS")
        subscribedGroups = subscribedGroups ?: emptyList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 7200
    }
}