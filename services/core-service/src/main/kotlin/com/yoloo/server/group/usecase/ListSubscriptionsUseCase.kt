package com.yoloo.server.group.usecase

import com.google.cloud.datastore.Cursor
import com.google.cloud.datastore.QueryResults
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.group.mapper.SubscriptionUserResponseMapper
import com.yoloo.server.group.vo.SubscriptionUserResponse

class ListSubscriptionsUseCase(private val subscriptionUserResponseMapper: SubscriptionUserResponseMapper) {

    fun execute(groupId: Long, cursor: String?): CollectionResponse<SubscriptionUserResponse> {
        val queryResults = getQueryResultIterator(groupId, cursor)

        return queryResults
            .asSequence()
            .map { subscriptionUserResponseMapper.apply(it) }
            .toList()
            .let {
                CollectionResponse.builder<SubscriptionUserResponse>()
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(queryResults.cursorAfter.toUrlSafe())
                    .build()
            }
    }

    private fun getQueryResultIterator(
        groupId: Long,
        cursor: String?
    ): QueryResults<Subscription> {
        var query = ofy()
            .load()
            .type(Subscription::class.java)
            .filter(Subscription.INDEX_GROUP_ID, groupId)

        cursor?.let { query = query.startAt(Cursor.fromUrlSafe(it)) }

        return query.limit(50).iterator()
    }
}
