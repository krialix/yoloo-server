package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.usecase.PatchUserUseCase
import com.yoloo.server.user.domain.usecase.contract.PatchUserContract
import org.springframework.stereotype.Service

@Service
class PatchUserUseCaseImpl : PatchUserUseCase {

    override fun execute(request: PatchUserContract.Request) {
        val user = ofy().load().type(User::class.java).id(request.userId).now()

        if (user == null || !user.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        val patchUserRequest = request.patchUserRequest

        patchUserRequest.displayName?.let { user.displayName.value = it }
        patchUserRequest.email?.let { user.email.value = it }

        ofy().save().entity(user)
    }
}