package com.yoloo.server.group.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import net.cinnom.nanocuckoo.NanoCuckooFilter

class UnsubscribeUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, groupId: Long) {
        val userKey = User.createKey(requesterId)
        val groupKey = Group.createKey(groupId)
        val subscriptionKey = Subscription.createKey(requesterId, groupId)

        val map = ofy().load().keys(userKey, groupKey, subscriptionKey) as Map<*, *>

        val user = map[userKey] as User
        val group = map[groupKey] as Group?
        val subscription = map[subscriptionKey] as Subscription?

        ServiceExceptions.checkNotFound(group != null, "group.not_found")
        ServiceExceptions.checkNotFound(subscription != null, "subscription.not_found")

        val deleteResult = ofy().delete().key(subscriptionKey)

        val subscriptionFilter = getSubscriptionFilter()
        subscriptionFilter.delete(subscription!!.id)
        val putFuture = memcacheService.put(Subscription.KEY_FILTER_SUBSCRIPTION, subscriptionFilter)

        group!!.countData.subscriberCount = group.countData.subscriberCount.dec()

        user.subscribedGroups = user.subscribedGroups.dropWhile { it.id == groupId }

        val saveResult = ofy().save().entities(group, user)

        TestUtil.saveNow(deleteResult, saveResult)
        TestUtil.saveNow(putFuture)
    }

    private fun getSubscriptionFilter(): NanoCuckooFilter {
        return memcacheService.get(Subscription.KEY_FILTER_SUBSCRIPTION) as NanoCuckooFilter
    }
}