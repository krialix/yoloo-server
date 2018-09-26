package com.yoloo.server.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
@Entity
data class EntityCache(
    @Id var id: String = KEY_ENTITY_CACHE,

    private var cuckooFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(1_000_000L).build()
) : Keyable<EntityCache> {

    fun add(key: String) {
        cuckooFilter.insert(key)
    }

    fun delete(key: String) {
        cuckooFilter.delete(key)
    }

    fun contains(key: Long): Boolean {
        return contains(key.toString())
    }

    fun contains(key: String): Boolean {
        return cuckooFilter.contains(key)
    }

    companion object {
        const val KEY_ENTITY_CACHE = "cache:entity"

        fun create(): EntityCache {
            return EntityCache()
        }

        @JvmStatic
        fun createId(): String {
            return KEY_ENTITY_CACHE
        }

        @JvmStatic
        fun createKey(): Key<EntityCache> {
            return Key.create(EntityCache::class.java, createId())
        }
    }
}
