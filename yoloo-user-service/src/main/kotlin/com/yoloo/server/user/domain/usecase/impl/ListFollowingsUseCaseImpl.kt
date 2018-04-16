package com.yoloo.server.user.domain.usecase.impl

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.common.Mapper
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.user.domain.usecase.ListFollowingsUseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowingsUseCaseContract
import org.dialectic.jsonapi.links.PaginationLinks
import org.dialectic.jsonapi.response.JsonApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListFollowingsUseCaseImpl @Autowired constructor(
    private val relationshipMapper: Mapper<Relationship, RelationshipResponse>
) : ListFollowingsUseCase {

    override fun execute(request: ListFollowingsUseCaseContract.Request): ListFollowingsUseCaseContract.Response {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(Relationship.FROM_ID, request.userId)
            .orderKey(true)

        query = request.cursor?.let { query.startAt(Cursor.fromWebSafeString(it)) }

        query = query.limit(ListFollowersUseCaseImpl.DEFAULT_LIST_LIMIT)

        val iterator = query.iterator()

        val data = iterator.asSequence().map(relationshipMapper::apply).toList()

        val cursor = iterator.cursor.toWebSafeString()

        val response =
            JsonApi.data(data).withLinks(PaginationLinks.links(null, null, cursor, null))

        return ListFollowingsUseCaseContract.Response(response)
    }
}