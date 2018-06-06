package com.yoloo.server.activity.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.activity.vo.ActionObject
import com.yoloo.server.activity.vo.Actor
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
@Entity
class Activity(
    @Id
    private var id: String,

    var actor: Actor,

    var activityObject: ActionObject,

    var type: String
) : BaseEntity<String, Activity>() {

    override fun getId(): String {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Activity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val KEY_FILTER_ACTIVITY = "FILTER_ACTIVITY"

        const val INDEX_ACTOR_ID = "actor.id"
        const val INDEX_ACTIVITY_OBJECT_ID = "activityObject.id"

        fun createKey(actorId: Long, objectId: Long, type: String): Key<Activity> {
            return Key.create(Activity::class.java, createId(actorId, objectId, type))
        }

        fun createId(actorId: Long, objectId: Long, type: String): String {
            return "$actorId:$objectId:$type"
        }

        fun extractActorId(activityId: String): Long {
            return activityId.substring(0, activityId.indexOf(':')).toLong()
        }

        fun extractObjectId(activityId: String): Long {
            return activityId.substring(activityId.indexOf(':') + 1, activityId.lastIndexOf(':')).toLong()
        }

        fun extractType(activityId: String): String {
            return activityId.substring(activityId.lastIndexOf(':') + 1)
        }

        fun exists(filter: NanoCuckooFilter, actorId: Long, objectId: Long, type: String): Boolean {
            return filter.contains(createId(actorId, objectId, type))
        }
    }
}