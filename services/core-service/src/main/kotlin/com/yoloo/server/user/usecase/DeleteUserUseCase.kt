package com.yoloo.server.user.usecase

import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.user.entity.User
import org.springframework.stereotype.Service

@Service
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
