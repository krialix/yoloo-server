package com.yoloo.server.user.domain.vo

import net.cinnom.nanocuckoo.NanoCuckooFilter

data class UserFilterData(
    var subscribedGroupsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .build(),

    var postsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .withCountingEnabled(true)
        .build(),

    var commentsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .withCountingEnabled(true)
        .build(),

    var bookmarkedFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .build(),

    var followedUsersFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .withCountingEnabled(true)
        .build()
)