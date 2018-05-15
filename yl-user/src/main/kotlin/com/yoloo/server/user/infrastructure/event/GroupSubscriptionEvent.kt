package com.yoloo.server.user.infrastructure.event

import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.DisplayName
import org.springframework.context.ApplicationEvent

class GroupSubscriptionEvent(
    source: Any,
    val eventId: Long,
    val userId: Long,
    val displayName: DisplayName,
    val avatarImage: AvatarImage,
    val groupIds: List<Long>
) : ApplicationEvent(source)
