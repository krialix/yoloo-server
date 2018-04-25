package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfTrue
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.*
import java.time.LocalDateTime
import javax.validation.Valid

@Cache
@NoArg
@Entity
data class User constructor(
    @Id
    var id: Long,

    var displayName: UserDisplayName,

    var url: Url? = null,

    var provider: SocialProvider,

    @field:Valid
    var email: Email,

    var image: AvatarImage,

    var password: Password? = null,

    var gender: Gender,

    @field:Valid
    var lastKnownIP: IP,

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

    var locale: UserLocale,

    var onlineStatus: OnlineStatus = OnlineStatus.ONLINE,

    var about: About? = null,

    var website: Url? = null,

    var lastPostTime: LocalDateTime? = null,

    @field:Valid
    var countData: UserCountData = UserCountData(),

    var userFilterData: UserFilterData = UserFilterData(),

    var subscribedGroups: List<UserGroup>
) : Validatable, Keyable<User> {

    @OnSave
    override fun validate() {
        super.validate()
    }
}