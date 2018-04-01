package com.yoloo.server.user.domain.vo

import net.cinnom.nanocuckoo.NanoCuckooFilter

data class UserFilterData(
    private var subscribedGroupsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .build(),

    private var postsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .withCountingEnabled(true)
        .build(),

    private var commentsFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .withCountingEnabled(true)
        .build(),

    private var bookmarkedFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .build(),

    private var followedUsersFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(32)
        .withCountingEnabled(true)
        .build()
)