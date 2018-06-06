package com.yoloo.server.user.queue.pull

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskHandle
import com.googlecode.objectify.cmd.Query
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.user.entity.Relationship
import com.yoloo.server.user.entity.User
import com.yoloo.server.user.event.RelationshipEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RelationshipPullServlet(
    @Qualifier("relationship-queue") private val queue: Queue,
    private val mapper: ObjectMapper
) : HttpServlet() {

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val tasks = queue.leaseTasks(3600, TimeUnit.SECONDS, NUMBER_OF_TASKS_TO_LEASE)

        processTasks(tasks, queue, mapper)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RelationshipPullServlet::class.java)

        private const val NUMBER_OF_TASKS_TO_LEASE = 500L

        private fun processTasks(tasks: List<TaskHandle>, q: Queue, mapper: ObjectMapper) {
            if (tasks.isEmpty()) {
                LOGGER.info("Task Queue has no tasks available for lease.")
                return
            }

            val pendingSaves = mutableListOf<Any>()

            val incCountUserIds = mutableSetOf<Long>()
            val decCountUserIds = mutableSetOf<Long>()

            var query: Query<Relationship> = ofy().load().type(Relationship::class.java)

            for (task in tasks) {
                if (task.name == RelationshipEvent.Follow::class.java.simpleName) {
                    val payload =
                        mapper.readValue(task.payload, RelationshipEvent.Follow.Payload::class.java)
                    LOGGER.info("Processing: taskName='{}'  payload='{}'", task.name, payload)

                    val relationship = createRelationship(payload)

                    pendingSaves.add(relationship)
                    incCountUserIds.add(payload.toUserId)
                } else if (task.name == RelationshipEvent.Unfollow::class.java.simpleName) {
                    val payload = mapper.readValue(
                        task.payload,
                        RelationshipEvent.Unfollow.Payload::class.java
                    )
                    LOGGER.info("Processing: taskName='{}'  payload='{}'", task.name, payload)

                    query = query.filter(Relationship.INDEX_FROM_ID, payload.fromUserId)
                        .filter(Relationship.INDEX_TO_ID, payload.toUserId)

                    decCountUserIds.add(payload.toUserId)
                }
            }

            val fetchIds = mutableListOf<Long>()
            fetchIds.addAll(incCountUserIds)
            fetchIds.addAll(decCountUserIds)

            ofy()
                .load()
                .type(User::class.java)
                .ids(fetchIds)
                .values
                .map {
                    if (incCountUserIds.contains(it.id)) {
                        it.profile.countData.followerCount.inc()
                    } else {
                        it.profile.countData.followerCount.dec()
                    }
                    return@map it
                }
                .let(pendingSaves::addAll)

            ofy().save().entities(pendingSaves)
            if (decCountUserIds.isNotEmpty()) {
                ofy().delete().keys(query.keys().list())
            }

            q.deleteTaskAsync(tasks)

            LOGGER.info("Processed and deleted ${tasks.size} tasks from the task queue (max: $NUMBER_OF_TASKS_TO_LEASE).")
        }

        private fun createRelationship(payload: RelationshipEvent.Follow.Payload): Relationship {
            return Relationship(
                id = Relationship.createId(payload.fromUserId, payload.toUserId),
                fromId = payload.fromUserId,
                toId = payload.toUserId,
                displayName = payload.fromDisplayName,
                avatarImage = payload.fromAvatarImage
            )
        }
    }
}
