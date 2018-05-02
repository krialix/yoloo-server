package com.yoloo.server.user.infrastructure.event

import com.yoloo.server.user.domain.vo.AvatarImage
import com.yoloo.server.user.domain.vo.UserDisplayName
import org.springframework.context.ApplicationEvent

class GroupSubscriptionEvent(
    source: Any,
    val userInfo: GroupSubscriptionEvent.UserInfo,
    val groupIds: List<Long>
) : ApplicationEvent(source) {

    data class UserInfo(val userId: Long, val displayName: UserDisplayName, val avatarImage: AvatarImage)
}
