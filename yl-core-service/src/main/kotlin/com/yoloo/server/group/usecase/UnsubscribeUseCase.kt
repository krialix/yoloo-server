package com.yoloo.server.group.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.memcache.MemcacheService
import com.yoloo.server.group.entity.Group
import com.yoloo.server.group.entity.Subscription
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.common.exception.exception.ServiceExceptions
import net.cinnom.nanocuckoo.NanoCuckooFilter
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate

class UnsubscribeUseCase(
    private val memcacheService: MemcacheService,
    private val pubSubTemplate: PubSubTemplate,
    private val objectMapper: ObjectMapper
) {

    fun execute(requesterId: Long, groupId: Long) {
        val group = ofy().load().type(Group::class.java).id(groupId).now()

        ServiceExceptions.checkNotFound(group != null, "group.not_found")

        val subscriptionFilter = getSubscriptionFilter()
        val subscribed = Subscription.isSubscribed(subscriptionFilter, requesterId, groupId)

        ServiceExceptions.checkNotFound(subscribed, "group.subscription.not_found")

        group.countData.subscriberCount = group.countData.subscriberCount--

        ofy().save().entity(group)

        val subscriptionKey = Subscription.createKey(requesterId, groupId)

        ofy().delete().key(subscriptionKey)

        publishGroupUnsubscribedEvent(group)
    }

    private fun getSubscriptionFilter(): NanoCuckooFilter {
        return memcacheService.get(Subscription.KEY_FILTER_SUBSCRIPTION) as NanoCuckooFilter
    }

    private fun publishGroupUnsubscribedEvent(group: Group) {
        val json = objectMapper.writeValueAsString(group)
        pubSubTemplate.publish("group.unsubscribed", json, null)
    }
}