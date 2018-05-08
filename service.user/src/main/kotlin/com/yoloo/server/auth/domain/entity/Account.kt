package com.yoloo.server.auth.domain.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.api.exception.ConflictException
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.ServiceExceptions.checkNotFound
import com.yoloo.server.user.domain.vo.Email
import com.yoloo.server.user.domain.vo.IP
import com.yoloo.server.user.domain.vo.Password
import com.yoloo.server.user.domain.vo.SocialProvider
import java.time.LocalDateTime

@Cache(expirationSeconds = 3600)
@Entity
@NoArg
data class Account(
    @Id var id: String,

    var provider: SocialProvider,

    @Index
    var email: Email,

    var password: Password? = null,

    var lastKnownIP: IP,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var disabled: Boolean = false,

    var scopes: Set<String>,

    var lastLoginTime: LocalDateTime? = null,

    var deletedAt: LocalDateTime? = null
) {

    companion object {
        const val INDEX_EMAIL = "email.value"

        @Throws(ConflictException::class)
        fun checkUserExistsAndEnabled(account: Account?) {
            checkNotFound(account != null, "user.error.not-found")
            checkNotFound(!account!!.disabled, "user.error.not-found")
        }
    }
}