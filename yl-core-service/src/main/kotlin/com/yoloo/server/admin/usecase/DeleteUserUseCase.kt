package com.yoloo.server.admin.usecase

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User

class DeleteUserUseCase {

    fun execute(userId: String) {
        val user = ofy().load().type(User::class.java).id(userId).now()

        /*user.apply {
            account.disabled = false
            account.deletedAt = LocalDateTime.now()
        }*/

        ofy().transact { ofy().save().entity(user) }
    }
}