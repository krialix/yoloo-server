package com.yoloo.server.admin.domain.usecase

import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import org.springframework.stereotype.Component
import java.security.Principal
import java.time.LocalDateTime

@Component
class DeleteUserUseCase : UseCase<DeleteUserUseCase.Request, Unit> {

    override fun execute(request: Request) {
        val user = ofy().load().type(User::class.java).id(request.userId).now()

        if (user == null || !user.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        user.apply {
            enabled = false
            deletedAt = LocalDateTime.now()
        }

        ofy().transact { ofy().save().entity(user) }
    }

    class Request(val principal: Principal, val userId: String)
}