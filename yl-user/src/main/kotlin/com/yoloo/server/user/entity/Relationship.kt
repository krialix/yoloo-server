package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.shared.Keyable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.user.vo.DisplayName

@NoArg
@Entity
data class Relationship(
    @Id
    var id: Long,

    @Index
    var fromId: Long,

    @Index
    var toId: Long,

    var displayName: DisplayName,

    var avatarImage: AvatarImage
) : Keyable<Relationship> {

    companion object {
        const val INDEX_FROM_ID = "fromId"
        const val INDEX_TO_ID = "toId"
    }
}