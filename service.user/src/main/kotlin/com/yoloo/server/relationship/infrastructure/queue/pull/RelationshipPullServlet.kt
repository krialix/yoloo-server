package com.yoloo.server.relationship.infrastructure.queue.pull

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.TaskHandle
import com.yoloo.server.common.id.generator.LongIdGenerator
import com.yoloo.server.objectify.ObjectifyProxy.ofy
import com.yoloo.server.relationship.domain.entity.Relationship
import com.yoloo.server.user.infrastructure.eventlistener.RelationshipEventListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RelationshipPullServlet(
    @Qualifier("relationship-queue") private val queue: Queue,
    private val mapper: ObjectMapper,
    @Qualifier("cached") private val idGenerator: LongIdGenerator
) : HttpServlet() {

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val tasks = queue.leaseTasks(3600, TimeUnit.SECONDS, NUMBER_OF_TASKS_TO_LEASE.toLong())

        processTasks(tasks, queue, mapper, idGenerator)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RelationshipPullServlet::class.java)
        private const val NUMBER_OF_TASKS_TO_LEASE = 100

        private fun processTasks(
            tasks: List<TaskHandle>,
            q: Queue,
            mapper: ObjectMapper,
            idGenerator: LongIdGenerator
        ) {
            var numberOfDeletedTasks = 0

            val relationships = mutableListOf<Relationship>()
            for (task in tasks) {
                val payload = mapper.readValue(task.payload, RelationshipEventListener.Payload::class.java)
                log.info("Processing: taskName='{}'  payload='{}'", task.name, payload)
                log.info("Deleting taskName='{}'", task.name)

                for (toId in payload.idFcmTokenMap.keys) {
                    val relationship = Relationship(
                        id = idGenerator.generateId(),
                        fromId = payload.userId,
                        toId = toId,
                        displayName = payload.displayName,
                        avatarImage = payload.avatarImage
                    )

                    relationships.add(relationship)
                }

                q.deleteTask(task)
                numberOfDeletedTasks++
            }

            ofy().save().entities(relationships)

            val message = if (numberOfDeletedTasks > 0)
                "Processed and deleted $NUMBER_OF_TASKS_TO_LEASE tasks from the  task queue."
            else
                "Task Queue has no tasks available for lease."

            log.info(message)
        }
    }
}
