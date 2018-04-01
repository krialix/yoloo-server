package com.yoloo.server.user.domain.model

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.NamespacedKeyProvider
import com.yoloo.server.common.util.NoArg

@NoArg
@Entity
data class UserBookmarkedPost(@Id private var id: String) : Keyable<UserBookmarkedPost> {

    companion object : NamespacedKeyProvider {
        override fun createNamespacedId(identifierId: String): String {
            return identifierId
        }
    }
}