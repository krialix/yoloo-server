package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.api.exception.NotFoundException
import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.requestpayload.PatchUserPayload
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class PatchUserUseCase : UseCase<PatchUserUseCase.Request, Unit> {

    override fun execute(request: Request) {
        val user = ofy().load().type(User::class.java).id(request.userId).now()

        if (user == null || !user.account.enabled) {
            throw NotFoundException("user.error.not-found")
        }

        val payload = request.payload

        payload.displayName?.let { user.profile.displayName.value = it }
        payload.email?.let { user.account.email.value = it }

        ofy().save().entity(user)
    }

    class Request(val principal: Principal?, val userId: String, val payload: PatchUserPayload)
}