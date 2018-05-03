package com.yoloo.server.user.infrastructure.event

import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.domain.vo.DisplayName
import org.springframework.context.ApplicationEvent

class RelationshipEvent(
    source: Any,
    val eventId: Long,
    val userId: Long,
    val displayName: DisplayName,
    val avatarImage: AvatarImage,
    val idFcmTokenMap: Map<Long, String>
) : ApplicationEvent(source)