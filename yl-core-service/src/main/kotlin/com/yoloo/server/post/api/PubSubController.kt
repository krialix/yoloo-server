package com.yoloo.server.post.api

import com.yoloo.server.common.util.NoArg
import org.slf4j.LoggerFactory
import org.springframework.cloud.gcp.pubsub.PubSubAdmin
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/test/pubsub")
@RestController
class PubSubController(private val pubSubTemplate: PubSubTemplate, private val pubSubAdmin: PubSubAdmin) {

    @PostMapping("/topics")
    fun createTopic(@RequestBody request: CreateTopicRequest) {
        pubSubAdmin.createTopic(request.topicName)
    }

    @DeleteMapping("/topics/{topic}")
    fun deleteTopic(@PathVariable("topic") topicName: String) {
        pubSubAdmin.deleteTopic(topicName)
    }

    @PostMapping("/topics/{topic}/publish")
    fun publish(@PathVariable("topic") topicName: String, @RequestBody request: PublishRequest) {
        pubSubTemplate.publish(topicName, request.message, null)
    }

    @PostMapping("/subscriptions")
    fun createSubscription(@RequestBody request: CreateSubscriptionRequest) {
        if (request.endpoint == null) {
            pubSubAdmin.createSubscription(request.subscriptionName, request.topicName)
        } else {
            pubSubAdmin.createSubscription(
                request.subscriptionName,
                request.topicName,
                request.endpoint
            )
        }
    }

    @DeleteMapping("/subscriptions/{subscription}")
    fun deleteSubscription(@PathVariable("subscription") subscriptionName: String) {
        pubSubAdmin.deleteSubscription(subscriptionName)
    }

    @PostMapping("/subscriptions/{subscription}/subscribe")
    fun subscribe(@PathVariable("subscription") subscription: String) {
        pubSubTemplate.subscribe(subscription) { pubsubMessage, ackReplyConsumer ->
            LOGGER.info(
                "Message received from $subscription subscription. ${pubsubMessage.data.toStringUtf8()}"
            )
            ackReplyConsumer.ack()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PubSubController::class.java)
    }

    @NoArg
    data class CreateTopicRequest(val topicName: String)

    @NoArg
    data class PublishRequest(val message: String)

    @NoArg
    data class CreateSubscriptionRequest(
        val topicName: String,
        val subscriptionName: String,
        val endpoint: String?
    )
}