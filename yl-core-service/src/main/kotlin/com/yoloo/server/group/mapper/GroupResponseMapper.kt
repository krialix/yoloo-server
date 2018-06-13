package com.yoloo.server.group.mapper

import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.vo.GroupCountResponse
import com.yoloo.server.group.vo.GroupResponse

class GroupResponseMapper {

    fun apply(from: Group, subscribed: Boolean): GroupResponse {
        return GroupResponse(
            id = from.id,
            displayName = from.displayName.value,
            description = from.description.value,
            count = GroupCountResponse(
                posts = from.countData.postCount,
                subscribers = from.countData.subscriberCount
            ),
            subscribed = subscribed
        )
    }
}