package com.yoloo.server.auth.domain.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.*
import java.time.LocalDateTime

@Cache(expirationSeconds = 3600)
@Entity
@NoArg
data class OauthUser(
    // oauth:userId
    @Id var id: String,

    var provider: SocialProvider,

    @Index
    var email: Email,

    var password: Password? = null,

    var displayName: DisplayName,

    var image: AvatarImage,

    var lastKnownIP: IP,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var disabled: Boolean = false,

    var scopes: Set<String>,

    var lastLoginTime: LocalDateTime? = null,

    var deletedAt: LocalDateTime? = null

    // TODO add client id
) {

    companion object {
        const val INDEX_EMAIL = "email.value"
    }
}