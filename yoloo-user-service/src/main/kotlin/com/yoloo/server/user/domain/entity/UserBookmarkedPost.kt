package com.yoloo.server.user.domain.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.NamespaceKeyProvider
import com.yoloo.server.common.util.NoArg

@NoArg
@Entity
data class UserBookmarkedPost(@Id private var id: String) : Keyable<UserBookmarkedPost> {

    companion object : NamespaceKeyProvider<UserBookmarkedPost> {
        override fun createNamespaceId(identifierId: String): String {
            return identifierId
        }

        override fun createNamespaceKey(identifierId: String): Key<UserBookmarkedPost> {
            return Key.create(UserBookmarkedPost::class.java, createNamespaceId(identifierId))
        }
    }
}