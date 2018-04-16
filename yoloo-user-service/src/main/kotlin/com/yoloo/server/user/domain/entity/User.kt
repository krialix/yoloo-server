package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfTrue
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.RegexUtil
import com.yoloo.server.common.util.TimestampIdGenerator
import com.yoloo.server.user.domain.vo.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Pattern

@Cache
@NoArg
@Entity
data class User constructor(
    @Id
    var id: String = TimestampIdGenerator.generateId(),

    var displayName: UserDisplayName,

    @field:Valid
    var email: Email,

    var avatarUrl: AvatarUrl,

    var password: Password? = null,

    @field:Pattern(regexp = RegexUtil.IP_REGEXP, message = "users-4")
    var lastKnownIP: String,

    var fcmToken: String,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    @Index(IfTrue::class)
    var enabled: Boolean = true,

    var scopes: Set<String>,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = createdAt,

    var deletedAt: LocalDateTime? = null,

    var locale: Locale,

    var onlineStatus: OnlineStatus = OnlineStatus.ONLINE,

    var countryIsoCode: String,

    var bio: String? = null,

    var website: String? = null,

    var lastPostTime: LocalDateTime? = null,

    @field:Valid
    var countData: UserCountData = UserCountData(),

    var userFilterData: UserFilterData = UserFilterData(),

    // Fields are used for mapping

    @Ignore
    var self: Boolean = false,

    @Ignore
    var following: Boolean = false
) : Validatable, Keyable<User> {

    @OnSave
    override fun validate() {
        super.validate()
    }
}