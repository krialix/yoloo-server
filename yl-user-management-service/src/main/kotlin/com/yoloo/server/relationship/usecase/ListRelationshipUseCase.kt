package com.yoloo.server.relationship.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.datastore.QueryResultIterator
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.entity.Relationship
import com.yoloo.server.relationship.usecase.ListRelationshipUseCase.Type.FOLLOWER
import com.yoloo.server.relationship.usecase.ListRelationshipUseCase.Type.FOLLOWING
import com.yoloo.server.user.vo.RelationshipResponse

class ListRelationshipUseCase {

    fun execute(
        type: Type,
        userId: String,
        cursor: String?
    ): CollectionResponse<RelationshipResponse> {
        val queryResultIterator = getQueryResultIterator(type, userId, cursor)

        return when (type) {
            FOLLOWING -> processFollowingList(queryResultIterator)
            FOLLOWER -> processFollowerList(queryResultIterator)
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
                    it.displayName.value,
                    it.avatarImage.url.value
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
                    it.displayName.value,
                    it.avatarImage.url.value
                )
            }
            .toList()
    }

    private fun getQueryResultIterator(
        type: Type,
        userId: String,
        cursor: String?
    ): QueryResultIterator<Relationship> {
        var query = ofy()
            .load()
            .type(Relationship::class.java)
            .filter(getFilterKey(type), userId)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        return query.limit(50).iterator()
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