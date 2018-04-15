package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.UserDisplayName

@NoArg
@Entity
data class Relationship(
    @Id
    var id: String,

    @Index
    var fromId: String,

    @Index
    var toId: String,

    var displayName: UserDisplayName,

    var avatarUrl: String
) : Keyable<Relationship> {

    companion object {
        const val FROM_ID = "fromId"
        const val TO_ID = "toId"
    }
}