package com.yoloo.server.group.usecase

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.datastore.QueryResultIterator
import com.yoloo.server.common.vo.CollectionResponse
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.group.mapper.SubscriptionUserResponseMapper
import com.yoloo.server.group.vo.SubscriptionUserResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy

class ListSubscriptionsUseCase(private val subscriptionUserResponseMapper: SubscriptionUserResponseMapper) {

    fun execute(groupId: Long, cursor: String?): CollectionResponse<SubscriptionUserResponse> {
        val queryResultIterator = getQueryResultIterator(groupId, cursor)

        return queryResultIterator
            .asSequence()
            .map { subscriptionUserResponseMapper.apply(it) }
            .toList()
            .let {
                CollectionResponse.builder<SubscriptionUserResponse>()
                    .data(it)
                    .prevPageToken(cursor)
                    .nextPageToken(queryResultIterator.cursor.toWebSafeString())
                    .build()
            }
    }

    private fun getQueryResultIterator(
        groupId: Long,
        cursor: String?
    ): QueryResultIterator<Subscription> {
        var query = ofy()
            .load()
            .type(Subscription::class.java)
            .filter(Subscription.INDEX_GROUP_ID, groupId)

        cursor?.let { query = query.startAt(Cursor.fromWebSafeString(it)) }

        return query.limit(50).iterator()
    }
}