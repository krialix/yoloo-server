package com.yoloo.server.common

import com.google.cloud.datastore.Cursor
import com.googlecode.objectify.cmd.Query

fun <T> Query<T>.applyCursor(cursor: String?): Query<T> {
    return when (cursor) {
        null -> this
        else -> startAt(Cursor.fromUrlSafe(cursor))
    }
}
