package com.yoloo.server.admin.domain.usecase.impl

import com.yoloo.server.admin.domain.usecase.DeleteUserUseCase
import com.yoloo.server.admin.domain.usecase.contract.DeleteUserContract
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DeleteUserUseCaseImpl : DeleteUserUseCase {

    override fun execute(request: DeleteUserContract.Request) {
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
}