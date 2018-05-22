package com.yoloo.server.user.usecase

import com.googlecode.objectify.Key
import com.yoloo.server.auth.entity.Account
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.Email
import com.yoloo.server.user.vo.PatchUserRequest
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
internal class PatchUserUseCase {

    fun execute(requesterId: Long, request: PatchUserRequest) {
        val userKey = Key.create(User::class.java, requesterId)
        val accountKey = Key.create(Account::class.java, requesterId)

        val map = ofy().load().keys(userKey, accountKey) as Map<*, *>
        val user = map[userKey] as User
        val account = map[accountKey] as Account

        request.displayName?.let {
            user.profile.displayName.value = it
            account.displayName.value = it
        }
        request.email?.let {
            user.email = Email(it)
            account.email = Email(it)
        }

        ofy().transact {
            ofy().save().entities(user, account)
        }
    }
}