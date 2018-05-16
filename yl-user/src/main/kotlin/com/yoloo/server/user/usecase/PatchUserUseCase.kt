package com.yoloo.server.user.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.auth.entity.Account
import com.yoloo.server.auth.vo.JwtClaims
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.PatchUserRequest
import com.yoloo.server.user.vo.Email
import org.springframework.stereotype.Component

@Component
internal class PatchUserUseCase : UseCase<PatchUserUseCase.Params, Unit> {

    override fun execute(params: Params) {
        val userId = params.claims.sub
        val payload = params.request

        val userKey = Key.create(User::class.java, userId)
        val accountKey = Key.create(Account::class.java, userId)

        val map = ofy().load().keys(userKey, accountKey) as Map<*, *>
        val user = map[userKey] as User
        val account = map[accountKey] as Account

        payload.displayName?.let {
            user.profile.displayName.value = it
            account.displayName.value = it
        }
        payload.email?.let {
            user.email = Email(it)
            account.email = Email(it)
        }

        ofy().transact {
            ofy().save().entities(user, account)
        }
    }

    class Params(val claims: JwtClaims, val request: PatchUserRequest)
}