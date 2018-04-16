package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.NamespaceKeyProvider
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.UserGroup

@Cache
@NoArg
@Entity
data class UserGroupBucket(@Id var bucketId: String, var userGroups: List<UserGroup>) : Keyable<UserGroupBucket> {

    companion object : NamespaceKeyProvider<UserGroupBucket> {
        private const val NAMESPACE = "user-group"

        override fun createNamespaceId(identifierId: String): String {
            return "$NAMESPACE-$identifierId"
        }

        override fun createNamespaceKey(identifierId: String): Key<UserGroupBucket> {
            return Key.create(UserGroupBucket::class.java, createNamespaceId(identifierId))
        }
    }
}