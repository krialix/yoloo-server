package com.yoloo.server.user.domain.entity

import com.fasterxml.uuid.Generators
import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfNotNull
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.util.RegexUtil
import com.yoloo.server.user.domain.vo.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Pattern

@NoArg
@Entity
data class User constructor(
    @Id
    var id: String = Generators.timeBasedGenerator().generate().toString().replace("-", ""),

    var displayName: UserDisplayName,

    @field:Valid
    var email: Email,

    var avatarUrl: String,

    var password: String? = null,

    @field:Pattern(regexp = RegexUtil.IP_REGEXP, message = "users-4")
    var lastKnownIP: String,

    var fcmToken: String,

    var expired: Boolean = false,

    var credentialsExpired: Boolean = false,

    var locked: Boolean = false,

    var enabled: Boolean = true,

    var scopes: Set<String>,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = createdAt,

    @IgnoreSave(IfNull::class)
    @Index(IfNotNull::class)
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
    var followingCount: Int = 0,

    @Ignore
    var followerCount: Int = 0,

    @Ignore
    var self: Boolean = false,

    @Ignore
    var following: Boolean = false
) : Validatable, Keyable<User> {

    @OnSave
    override fun validate() {
        super.validate()
    }

    fun isDeleted(): Boolean {
        return deletedAt != null
    }
}