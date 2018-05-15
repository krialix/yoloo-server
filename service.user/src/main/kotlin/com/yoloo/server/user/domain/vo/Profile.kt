package com.yoloo.server.user.domain.vo

import com.yoloo.server.auth.domain.vo.UserLocale
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url

@NoArg
data class Profile(
    var displayName: DisplayName,

    var image: AvatarImage,

    var profileUrl: Url? = null,

    var gender: Gender,

    var spokenLanguages: List<@JvmSuppressWildcards Language> = emptyList(),

    var websiteUrl: Url? = null,

    var about: About? = null,

    var verified: Boolean = false,

    var locale: UserLocale,

    var countData: UserCountData = UserCountData()
)