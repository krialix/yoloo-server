package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.objectify.ObjectifyProxy
import com.yoloo.server.user.domain.entity.Relationship
import com.yoloo.server.user.domain.usecase.DeleteRelationshipUseCase
import com.yoloo.server.user.domain.usecase.contract.DeleteRelationshipUseCaseContract
import org.springframework.stereotype.Service

@Service
class DeleteRelationshipUseCaseImpl : DeleteRelationshipUseCase {

    override fun execute(request: DeleteRelationshipUseCaseContract.Request) {
        val relationshipKey = ObjectifyProxy.ofy()
            .load()
            .type(Relationship::class.java)
            .filter(Relationship.TO_ID, request.userId)
            .keys()
            .first()
            .now()

        ObjectifyProxy.ofy().delete().key(relationshipKey)
    }
}