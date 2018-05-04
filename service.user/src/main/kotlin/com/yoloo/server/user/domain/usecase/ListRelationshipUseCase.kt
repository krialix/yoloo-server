package com.yoloo.server.user.domain.usecase

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.relationship.domain.response.RelationshipResponse
import com.yoloo.server.relationship.infrastructure.mapper.RelationshipResponseMapper
import com.yoloo.server.user.domain.usecase.ListRelationshipUseCase.Type.FOLLOWER
import com.yoloo.server.user.domain.usecase.ListRelationshipUseCase.Type.FOLLOWING
import org.springframework.stereotype.Component

@Component
class ListRelationshipUseCase(
    private val relationshipMapper: RelationshipResponseMapper
) : UseCase<ListRelationshipUseCase.Request, CollectionResponse<RelationshipResponse>> {

    override fun execute(request: Request): CollectionResponse<RelationshipResponse> {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(getQueryKeyByRelationshipType(request.type), request.userId)
            .orderKey(true)

        request.cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        val keys = query.limit(50).keys().list()

        val cursor = Cursor.fromWebSafeString(keys.last().toWebSafeString()).toWebSafeString()

        return ofy()
            .load()
            .keys(keys)
            .values
            .asSequence()
            .map(relationshipMapper::apply)
            .toList()
            .let {
                CollectionResponse.builder<RelationshipResponse>()
                    .data(it)
                    .prevPageToken(request.cursor)
                    .nextPageToken(cursor)
                    .build()
            }
    }

    private fun getQueryKeyByRelationshipType(type: Type): String {
        return when (type) {
            FOLLOWING -> Relationship.FROM_ID
            FOLLOWER -> Relationship.TO_ID
        }
    }

    class Request(val type: Type, val userId: String, val cursor: String?)

    enum class Type {
        FOLLOWING,
        FOLLOWER
    }
}