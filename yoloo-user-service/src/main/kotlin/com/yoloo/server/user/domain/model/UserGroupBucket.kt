package com.yoloo.server.user.domain.model

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.NamespacedKeyProvider
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.UserGroup

@NoArg
@Entity
data class UserGroupBucket(@Id var bucketId: String, var userGroups: List<UserGroup>) : Keyable<UserGroupBucket> {

    companion object : NamespacedKeyProvider {
        private const val NAMESPACE = "usergroup"

        override fun createNamespacedId(identifierId: String?): String {
            return "$NAMESPACE-$identifierId"
        }
    }
}