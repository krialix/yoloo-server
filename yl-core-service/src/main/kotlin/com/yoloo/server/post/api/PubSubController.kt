package com.yoloo.server.post.api

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskOptions
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.config.PostQueueConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gcp.pubsub.PubSubAdmin
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RequestMapping(
    "/api/v1/pubsub",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class PubSubController(
    private val pubSubTemplate: PubSubTemplate,
    private val pubSubAdmin: PubSubAdmin,
    @Qualifier(PostQueueConfig.QUEUE_POST) private val postQueue: Queue
) {

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
        pubSubTemplate.subscribe(subscription, { pubsubMessage, ackReplyConsumer ->
            LOGGER.info(
                "Message received from $subscription subscription. ${pubsubMessage.data.toStringUtf8()}"
            )
            ackReplyConsumer.ack()
        })
    }

    @GetMapping("/queue/{message}")
    fun queue(@PathVariable("message") message: String) {
        postQueue.add(TaskOptions.Builder.withUrl("/api/v1/pubsub/queue/read").param("key", "key"))
    }

    @PostMapping("/queue/read")
    fun read(request: HttpServletRequest) {
        val key = request.getParameter("key")

        LOGGER.info("KEY: {}", key)
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