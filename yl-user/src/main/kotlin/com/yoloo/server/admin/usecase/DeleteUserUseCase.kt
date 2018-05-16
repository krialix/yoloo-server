package com.yoloo.server.admin.usecase

import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class DeleteUserUseCase : UseCase<DeleteUserUseCase.Request, Unit> {

    override fun execute(request: Request) {
        val user = ofy().load().type(User::class.java).id(request.userId).now()

        /*user.apply {
            account.disabled = false
            account.deletedAt = LocalDateTime.now()
        }*/

        ofy().transact { ofy().save().entity(user) }
    }

    class Request(val principal: Principal?, val userId: String)
}