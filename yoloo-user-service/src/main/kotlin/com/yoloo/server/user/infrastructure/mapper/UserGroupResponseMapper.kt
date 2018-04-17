package com.yoloo.server.user.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.user.domain.response.UserGroupResponse
import com.yoloo.server.user.domain.vo.UserGroup
import org.springframework.stereotype.Component

@Component
class UserGroupResponseMapper : Mapper<UserGroup, UserGroupResponse> {

    override fun apply(group: UserGroup): UserGroupResponse {
        return UserGroupResponse(id = group.id, displayName = group.displayName, imageUrl = group.imageUrl)
    }
}