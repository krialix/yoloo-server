package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.usecase.DeleteUserUseCase
import com.yoloo.server.user.domain.usecase.contract.DeleteUserUseCaseContract
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DeleteUserUseCaseImpl : DeleteUserUseCase {

    override fun execute(request: DeleteUserUseCaseContract.Request) {
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