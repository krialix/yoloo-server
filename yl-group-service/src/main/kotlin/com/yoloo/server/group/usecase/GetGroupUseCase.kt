package com.yoloo.server.group.usecase

import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.group.mapper.GroupResponseMapper
import com.yoloo.server.group.vo.GroupResponse
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter

class GetGroupUseCase(
    private val memcacheService: MemcacheService,
    private val groupResponseMapper: GroupResponseMapper
) {

    fun execute(requesterId: Long, groupId: Long): GroupResponse {
        val group = ofy().load().type(Group::class.java).id(groupId).now()

        ServiceExceptions.checkNotFound(group != null, "group.not_found")

        val subscriptionFilter = getSubscriptionFilter()
        val subscribed = Subscription.isSubscribed(subscriptionFilter, requesterId, groupId)

        return groupResponseMapper.apply(group, subscribed)
    }

    private fun getSubscriptionFilter(): NanoCuckooFilter {
        return memcacheService.get(Subscription.KEY_FILTER_SUBSCRIPTION) as NanoCuckooFilter
    }
}