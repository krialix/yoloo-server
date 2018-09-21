package com.yoloo.server.entity.service

interface EntityService {

    fun exists(key: String): Boolean {
        return exists(arrayOf(key))[key]!!
    }

    fun exists(keys: Array<String>): Map<String, Boolean> {
        return exists(keys.toList())
    }

    fun exists(keys: Iterable<String>): Map<String, Boolean>
}
