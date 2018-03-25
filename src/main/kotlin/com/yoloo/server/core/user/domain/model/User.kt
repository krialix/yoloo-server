package com.yoloo.server.core.user.domain.model

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.core.common.Validatable
import com.yoloo.server.core.util.NoArg
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@NoArg
@Entity
data class User constructor(
    @Id var id: Long? = null,

    @get:Valid
    var displayName: UserDisplayName,

    var avatarUrl: String,

    var bio: String? = null,

    var locale: Locale,

    var userMeta: UserMeta,

    @Index
    @get:Valid
    var email: Email,

    var scopes: Set<String>,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = createdAt,

    @Index var deletedAt: LocalDateTime? = null,

    var birthDate: LocalDate,

    var countData: UserCountData = UserCountData(),

    var onlineStatus: OnlineStatus = OnlineStatus.ONLINE,

    var countryIsoCode: String,

    var website: String? = null,

    var lastPostTime: LocalDateTime? = null,

    var subscribedGroups: List<UserGroup>
) : Validatable