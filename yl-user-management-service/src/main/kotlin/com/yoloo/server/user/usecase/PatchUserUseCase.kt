package com.yoloo.server.user.usecase

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.Email
import com.yoloo.server.user.vo.PatchUserRequest

class PatchUserUseCase {

    fun execute(requesterId: Long, request: PatchUserRequest) {
        val user = ofy().load().type(User::class.java).id(requesterId).now()

        ServiceExceptions.checkNotFound(user != null, "user.not_found")

        request.displayName?.let {
            user.profile.displayName.value = it
        }
        request.email?.let {
            user.email = Email(it)
        }

        ofy().transact {
            ofy().save().entity(user)
        }
    }
}