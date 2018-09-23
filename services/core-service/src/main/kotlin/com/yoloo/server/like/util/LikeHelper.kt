package com.yoloo.server.like.util

import net.cinnom.nanocuckoo.NanoCuckooFilter
import java.util.regex.Pattern

object LikeHelper {

    private val PATTERN_DELIMITER = Pattern.compile(":")

    fun createId(userId: Long, votableId: Long): String {
        return "like:$userId:$votableId"
    }

    private fun extractUserId(id: String): Long {
        return id.split(PATTERN_DELIMITER)[1].toLong()
    }

    private fun extractVotableId(id: String): Long {
        return id.split(PATTERN_DELIMITER)[2].toLong()
    }

    fun isVoted(filter: NanoCuckooFilter, userId: Long, votableId: Long): Boolean {
        return filter.contains(createId(userId, votableId))
    }
}