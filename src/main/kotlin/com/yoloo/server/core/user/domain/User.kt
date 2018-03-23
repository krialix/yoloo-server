package com.yoloo.server.core.user.domain

import com.google.appengine.api.datastore.Email
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.core.util.NoArg
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@NoArg
@Entity
data class User(
    @Id var id: Long,
    var displayName: UserDisplayName,
    var avatarUrl: String,
    var bio: String,
    var locale: Locale,
    var userMeta: UserMeta,
    var email: Email,
    var scopes: List<String>,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = createdAt,
    var deletedAt: LocalDateTime? = null,
    var birthDate: LocalDate,
    var countData: UserCountData = UserCountData(),
    var onlineStatus: OnlineStatus = OnlineStatus.ONLINE,
    var countryIsoCode: String,
    var website: String,
    var lastPostTime: LocalDateTime? = null,
    var subscribedGroups: List<UserGroup>
)