package com.yoloo.server.auth.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.auth.vo.Provider
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.vo.DisplayName
import com.yoloo.server.user.vo.Email
import com.yoloo.server.user.vo.IP
import com.yoloo.server.user.vo.Password
import java.time.LocalDateTime

@Cache(expirationSeconds = 3600)
@Entity
@NoArg
class Account(
    @Id var id: Long,

    var clientId: String,

    var provider: Provider,

    @Index
    var email: Email,

    var emailVerified: Boolean = false,

    var accountVerified: Boolean = false,

    var password: Password? = null,

    var displayName: DisplayName,

    var image: AvatarImage,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var disabled: Boolean = false,

    var authorities: Set<@JvmSuppressWildcards Authority>,

    var lastSignInTime: LocalDateTime? = null,

    // Metadata
    var localIp: IP,

    var fcmToken: String
) : BaseEntity<Long, Account>() {

    override fun getId(): Long {
        return id
    }

    override fun sameIdentityAs(other: Account): Boolean {
        return equals(other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Account

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
        const val KEY_FILTER_EMAIL = "FILTER_EMAIL"

        const val INDEX_EMAIL = "email.value"
    }

    enum class Authority {
        ANONYMOUS,
        MEMBER,
        ADMIN
    }
}