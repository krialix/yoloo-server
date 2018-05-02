package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.shared.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.Account
import com.yoloo.server.user.domain.vo.Profile
import com.yoloo.server.user.domain.vo.UserFilterData
import com.yoloo.server.user.domain.vo.UserGroup

@Cache
@NoArg
@Entity
data class User constructor(
    @Id var id: Long,

    var profile: Profile,

    var account: Account,

    var userFilterData: UserFilterData = UserFilterData(),

    var subscribedGroups: List<UserGroup>,

    // Extra fields for easy mapping
    val self: Boolean = false,

    val following: Boolean = false
) : BaseEntity<User>(1)