package com.yoloo.server.group.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.common.vo.AvatarImage
import com.yoloo.server.common.vo.Url
import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.group.vo.DisplayName
import com.yoloo.server.group.vo.GroupFlag
import com.yoloo.server.objectify.ObjectifyProxy
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.rest.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class SubscribeUseCase(
    private val memcacheService: MemcacheService,
    private val pubSubTemplate: PubSubTemplate,
    private val objectMapper: ObjectMapper
) {

    fun execute(requesterId: Long, requesterDisplayName: String, requesterAvatarUrl: String, groupId: Long) {
        val group = ObjectifyProxy.ofy().load().type(Group::class.java).id(groupId).now()

        ServiceExceptions.checkNotFound(group != null, "group.not_found")
        ServiceExceptions.checkForbidden(
            !group.flags.contains(GroupFlag.DISABLE_SUBSCRIPTION),
            "group.subscription.forbidden"
        )

        val subscriptionFilter = getSubscriptionFilter()
        val subscribed = Subscription.isSubscribed(subscriptionFilter, requesterId, groupId)

        ServiceExceptions.checkConflict(!subscribed, "group.subscription.conflict")

        val subscription = createSubscription(requesterId, groupId, requesterDisplayName, requesterAvatarUrl)

        group.countData.subscriberCount = group.countData.subscriberCount++

        ofy().save().entities(group, subscription)

        publishGroupSubscribedEvent(group)
    }

    private fun createSubscription(
        requesterId: Long,
        groupId: Long,
        requesterDisplayName: String,
        requesterAvatarUrl: String
    ): Subscription {
        return Subscription(
            id = Subscription.createId(requesterId, groupId),
            userId = requesterId,
            groupId = groupId,
            displayName = DisplayName(requesterDisplayName),
            avatarImage = AvatarImage(Url(requesterAvatarUrl))
        )
    }

    private fun getSubscriptionFilter(): NanoCuckooFilter {
        return memcacheService.get(Subscription.KEY_FILTER_SUBSCRIPTION) as NanoCuckooFilter
    }

    private fun publishGroupSubscribedEvent(group: Group) {
        val json = objectMapper.writeValueAsString(group)
        pubSubTemplate.publish("group.subscribed", json, null)
    }
}