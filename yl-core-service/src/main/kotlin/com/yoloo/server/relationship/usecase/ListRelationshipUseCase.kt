package com.yoloo.server.relationship.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.datastore.QueryResultIterator
import com.yoloo.server.common.vo.CollectionResponse
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.user.vo.RelationshipResponse
import org.springframework.stereotype.Service

@Service
class ListRelationshipUseCase {

    fun execute(
        type: Relationship.Type,
        userId: Long,
        cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        val queryResultIterator = getQueryResultIterator(type, userId, cursor)

        return when (type) {
            Relationship.Type.FOLLOWING -> processFollowingList(queryResultIterator)
            Relationship.Type.FOLLOWER -> processFollowerList(queryResultIterator)
        }.let {
            CollectionResponse.builder<RelationshipResponse>()
                .data(it)
                .prevPageToken(cursor)
                .nextPageToken(queryResultIterator.cursor.toWebSafeString())
                .build()
        }
    }

    private fun processFollowingList(qri: QueryResultIterator<Relationship>): List<RelationshipResponse> {
        return qri
            .asSequence()
            .map {
                RelationshipResponse(
                    Relationship.extractToId(it.id),
                    it.fromDisplayName.value,
                    it.fromProfileImageUrl.value
                )
            }
            .toList()
    }

    private fun processFollowerList(qri: QueryResultIterator<Relationship>): List<RelationshipResponse> {
        return qri
            .asSequence()
            .map {
                RelationshipResponse(
                    Relationship.extractFromId(it.id),
                    it.fromDisplayName.value,
                    it.fromProfileImageUrl.value
                )
            }
            .toList()
    }

    private fun getQueryResultIterator(
        type: Relationship.Type,
        userId: Long,
        cursor: String?
    ): QueryResultIterator<Relationship> {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(getFilterKey(type), userId)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        return query.limit(50).iterator()
    }

    private fun getFilterKey(type: Relationship.Type): String {
        return when (type) {
            Relationship.Type.FOLLOWING -> Relationship.INDEX_FROM_ID
            Relationship.Type.FOLLOWER -> Relationship.INDEX_TO_ID
        }
    }
}
