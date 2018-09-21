package com.yoloo.server.entity

import com.yoloo.server.common.vo.Url
import com.yoloo.server.user.vo.DisplayName

interface Relationable {
    fun follow(other: Relationable) {
        incFollowingCount()
        other.incFollowerCount()
    }

    fun unfollow(other: Relationable) {
        decFollowingCount()
        other.decFollowerCount()
    }

    fun incFollowerCount()

    fun incFollowingCount()

    fun decFollowerCount()

    fun decFollowingCount()

    fun getRelationableId(): Long

    fun getRelationableDisplayName(): DisplayName

    fun getRelationableProfileImageUrl(): Url
}
