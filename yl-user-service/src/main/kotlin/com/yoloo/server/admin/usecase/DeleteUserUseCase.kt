package com.yoloo.server.admin.usecase

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.security.Principal

@Lazy
@Component
class DeleteUserUseCase {

    fun execute(principal: Principal, userId: String) {
        val user = ofy().load().type(User::class.java).id(userId).now()

        /*user.apply {
            account.disabled = false
            account.deletedAt = LocalDateTime.now()
        }*/

        ofy().transact { ofy().save().entity(user) }
    }
}