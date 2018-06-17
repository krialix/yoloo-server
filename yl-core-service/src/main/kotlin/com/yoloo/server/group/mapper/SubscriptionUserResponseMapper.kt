package com.yoloo.server.group.mapper

import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.group.vo.SubscriptionUserResponse

class SubscriptionUserResponseMapper {

    fun apply(from: Subscription): SubscriptionUserResponse {
        return SubscriptionUserResponse(
            id = from.userId,
            displayName = from.displayName.value,
            imageUrl = from.avatarImage.url.value
        )
    }
}