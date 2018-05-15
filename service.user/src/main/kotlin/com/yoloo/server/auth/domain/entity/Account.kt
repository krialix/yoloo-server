package com.yoloo.server.auth.domain.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.auth.domain.vo.Provider
import com.yoloo.server.common.shared.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.DisplayName
import com.yoloo.server.user.domain.vo.Email
import com.yoloo.server.user.domain.vo.IP
import com.yoloo.server.user.domain.vo.Password
import java.time.LocalDateTime

@Cache(expirationSeconds = 3600)
@Entity
@NoArg
data class Account(
    @Id var id: Long,

    var provider: Provider,

    @Index
    var email: Email,

    var password: Password? = null,

    var displayName: DisplayName,

    var image: AvatarImage,

    var localIp: IP,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var disabled: Boolean = false,

    var authorities: Set<Authority>,

    var lastLoginTime: LocalDateTime? = null,

    var deletedAt: LocalDateTime? = null,

    var fcmToken: String

    // TODO add client id
) : BaseEntity<Account>(1) {

    companion object {
        const val INDEX_EMAIL = "email.value"
    }

    enum class Authority {
        ANONYMOUS,
        MEMBER,
        ADMIN
    }
}