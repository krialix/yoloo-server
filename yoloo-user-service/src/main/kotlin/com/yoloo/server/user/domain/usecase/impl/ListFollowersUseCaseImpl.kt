package com.yoloo.server.user.domain.usecase.impl

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.common.response.attachment.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.relationship.infrastructure.mapper.RelationshipResponseMapper
import com.yoloo.server.user.domain.usecase.ListFollowersUseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowersUseCaseContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListFollowersUseCaseImpl @Autowired constructor(
    private val relationshipMapper: RelationshipResponseMapper
) : ListFollowersUseCase {

    override fun execute(request: ListFollowersUseCaseContract.Request): ListFollowersUseCaseContract.Response {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(Relationship.TO_ID, request.userId)
            .orderKey(true)

        query = request.cursor?.let { query.startAt(Cursor.fromWebSafeString(it)) }

        query = query.limit(DEFAULT_LIST_LIMIT)

        val iterator = query.iterator()

        val data = iterator.asSequence().map(relationshipMapper::apply).toList()

        val cursor = iterator.cursor.toWebSafeString()

        val response = CollectionResponse.builder<RelationshipResponse>()
            .data(data)
            .prevPageToken(request.cursor)
            .nextPageToken(cursor)
            .build()

        return ListFollowersUseCaseContract.Response(response)
    }

    companion object {
        const val DEFAULT_LIST_LIMIT = 50
    }
}