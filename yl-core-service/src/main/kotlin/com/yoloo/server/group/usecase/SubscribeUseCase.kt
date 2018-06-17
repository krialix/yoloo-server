package com.yoloo.server.group.usecase

import com.google.appengine.api.memcache.AsyncMemcacheService
import com.yoloo.server.common.exception.exception.ServiceExceptions
import com.yoloo.server.common.util.TestUtil
import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.group.vo.GroupFlag
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.vo.UserGroup
import net.cinnom.nanocuckoo.NanoCuckooFilter

class SubscribeUseCase(private val memcacheService: AsyncMemcacheService) {

    fun execute(requesterId: Long, requesterDisplayName: String, requesterAvatarUrl: String, groupId: Long) {
        val userKey = User.createKey(requesterId)
        val groupKey = Group.createKey(groupId)

        val map = ofy().load().keys(userKey, groupKey) as Map<*, *>

        val user = map[userKey] as User
        val group = map[groupKey] as Group?

        ServiceExceptions.checkNotFound(group != null, "group.not_found")
        val subscriptionDisabled = group!!.flags.contains(GroupFlag.DISABLE_SUBSCRIPTION)
        ServiceExceptions.checkForbidden(!subscriptionDisabled, "group.subscription.forbidden")

        val subscriptionFilter = getSubscriptionFilter()
        val subscribed = Subscription.isSubscribed(subscriptionFilter, requesterId, groupId)

        ServiceExceptions.checkConflict(!subscribed, "group.subscription.conflict")

        val subscription = Subscription.create(requesterId, groupId, requesterDisplayName, requesterAvatarUrl)

        subscriptionFilter.insert(subscription.id)
        val putFuture = memcacheService.put(Subscription.KEY_FILTER_SUBSCRIPTION, subscriptionFilter)

        group.countData.subscriberCount = group.countData.subscriberCount.inc()

        val userGroup = createUserGroup(group)
        user.subscribedGroups = user.subscribedGroups.plus(userGroup)

        val saveResult = ofy().save().entities(group, subscription, user)

        TestUtil.saveResultsNowIfTest(saveResult)
        TestUtil.saveFuturesNowIfTest(putFuture)
    }

    private fun getSubscriptionFilter(): NanoCuckooFilter {
        return memcacheService.get(Subscription.KEY_FILTER_SUBSCRIPTION).get() as NanoCuckooFilter
    }

    private fun createUserGroup(group: Group) : UserGroup {
        return UserGroup(id = group.id, imageUrl = "", displayName = group.displayName.value)
    }
}