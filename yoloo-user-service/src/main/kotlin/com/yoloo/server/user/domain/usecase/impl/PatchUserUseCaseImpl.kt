package com.yoloo.server.user.domain.usecase.impl

import com.googlecode.objectify.Key
import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.usecase.PatchUserUseCase
import com.yoloo.server.user.domain.usecase.contract.PatchUserUseCaseContract
import org.springframework.stereotype.Service

@Service
class PatchUserUseCaseImpl : PatchUserUseCase {

    override fun execute(request: PatchUserUseCaseContract.Request) {
        val userKey = Key.create(User::class.java, request.userId)
        val user = ofy().load().key(userKey).now()

        if (user == null || user.isDeleted()) {
            throw NotFoundException("user.error.not-found")
        }

        val patchUserRequest = request.patchUserRequest

        patchUserRequest.displayName?.let { user.displayName.value = it }
        patchUserRequest.email?.let { user.email.value = it }

        ofy().save().entity(user)
    }
}