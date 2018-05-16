package com.yoloo.server.user.usecase

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.common.response.CollectionResponse
import com.yoloo.server.common.shared.UseCase
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.vo.RelationshipResponse
import com.yoloo.server.user.usecase.ListRelationshipUseCase.Type.FOLLOWER
import com.yoloo.server.user.usecase.ListRelationshipUseCase.Type.FOLLOWING
import org.springframework.stereotype.Component

@Component
class ListRelationshipUseCase : UseCase<ListRelationshipUseCase.Request, CollectionResponse<RelationshipResponse>> {

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
            .map {
                RelationshipResponse(
                    id = it.id,
                    displayName = it.displayName.value,
                    avatarUrl = it.avatarImage.url.value
                )
            }
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
            FOLLOWING -> Relationship.INDEX_FROM_ID
            FOLLOWER -> Relationship.INDEX_TO_ID
        }
    }

    class Request(val type: Type, val userId: String, val cursor: String?)

    enum class Type {
        FOLLOWING,
        FOLLOWER
    }
}