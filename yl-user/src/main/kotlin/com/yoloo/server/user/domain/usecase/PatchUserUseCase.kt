package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.requestpayload.PatchUserPayload
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class PatchUserUseCase : UseCase<PatchUserUseCase.Request, Unit> {

    override fun execute(request: Request) {
        val userId = request.principal?.name?.toLong()!!

        val user = ofy().load().type(User::class.java).id(userId).now()

        //User.checkUserExistsAndEnabled(user)

        val payload = request.payload

        payload.displayName?.let { user.profile.displayName.value = it }
        //payload.email?.let { user.account.email.value = it }

        ofy().save().entity(user)
    }

    class Request(val principal: Principal?, val payload: PatchUserPayload)
}