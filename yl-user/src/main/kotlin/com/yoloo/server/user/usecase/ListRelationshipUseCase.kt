package com.yoloo.server.user.usecase

import com.google.appengine.api.datastore.Cursor
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.usecase.ListRelationshipUseCase.Type.FOLLOWER
import com.yoloo.server.user.usecase.ListRelationshipUseCase.Type.FOLLOWING
import com.yoloo.server.user.vo.RelationshipResponse
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class ListRelationshipUseCase {

    fun execute(type: Type, userId: String, cursor: String?): CollectionResponse<RelationshipResponse> {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(getFilterKey(type), userId)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        val queryResultIterator = query.limit(50).iterator()

        return queryResultIterator
            .asSequence()
            .map {
                RelationshipResponse(
                    id = when (type) {
                        FOLLOWING -> Relationship.extractToId(it.id)
                        FOLLOWER -> Relationship.extractFromId(it.id)
                    },
                    displayName = it.displayName.value,
                    avatarUrl = it.avatarImage.url.value
                )
            }
            .toList()
            .let {
                CollectionResponse.builder<RelationshipResponse>()
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(queryResultIterator.cursor.toWebSafeString())
                    .build()
            }
    }

    private fun getFilterKey(type: Type): String {
        return when (type) {
            FOLLOWING -> Relationship.INDEX_FROM_ID
            FOLLOWER -> Relationship.INDEX_TO_ID
        }
    }

    enum class Type {
        FOLLOWING,
        FOLLOWER
    }
}