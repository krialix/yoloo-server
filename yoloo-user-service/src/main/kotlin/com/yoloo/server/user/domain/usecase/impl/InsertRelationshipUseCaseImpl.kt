package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.domain.entity.Relationship
import com.yoloo.server.user.domain.entity.User
import com.yoloo.server.user.domain.usecase.InsertRelationshipUseCase
import com.yoloo.server.user.domain.usecase.contract.InsertRelationshipUseCaseContract
import com.yoloo.server.user.infrastructure.util.IdGenerator
import org.springframework.stereotype.Service

@Service
class InsertRelationshipUseCaseImpl : InsertRelationshipUseCase {

    override fun execute(request: InsertRelationshipUseCaseContract.Request) {
        val id = IdGenerator.timestampUUID()
        val fromId = request.principal.name
        val toId = request.userId

        val toUser = ofy().load().type(User::class.java).id(toId).now()

        val relationship = Relationship(
            id = id,
            fromId = fromId,
            toId = toId,
            displayName = toUser.displayName,
            avatarUrl = toUser.avatarUrl
        )

        ofy().save().entity(relationship)
    }
}