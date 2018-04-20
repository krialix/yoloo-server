package com.yoloo.server.user.domain.usecase.impl

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.relationship.infrastructure.mapper.RelationshipResponseMapper
import com.yoloo.server.user.domain.usecase.ListFollowingsUseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowingsUseCaseContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListFollowingsUseCaseImpl @Autowired constructor(
    private val relationshipMapper: RelationshipResponseMapper
) : ListFollowingsUseCase {

    override fun execute(request: ListFollowingsUseCaseContract.Request): ListFollowingsUseCaseContract.Response {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(Relationship.FROM_ID, request.userId)
            .orderKey(true)

        request.cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        query = query.limit(ListFollowersUseCaseImpl.DEFAULT_LIST_LIMIT)

        val iterator = query.iterator()

        val data = iterator.asSequence().map(relationshipMapper::apply).toList()

        val cursor = iterator.cursor.toWebSafeString()

        val response = CollectionResponse.builder<RelationshipResponse>()
            .data(data)
            .prevPageToken(request.cursor)
            .nextPageToken(cursor)
            .build()

        return ListFollowingsUseCaseContract.Response(response)
    }
}