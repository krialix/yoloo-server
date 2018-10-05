package com.yoloo.server.filter

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.entity.Keyable
import net.cinnom.nanocuckoo.NanoCuckooFilter

@NoArg
@Entity
data class EntityIdFilter(
    @Id var id: String = ENTITY_ID,

    private var cuckooFilter: NanoCuckooFilter = NanoCuckooFilter.Builder(1_000_000L).build()
) : Keyable<EntityIdFilter>, Filter {

    override fun toFilterKey(): Key<*> {
        return createKey()
    }

    fun add(key: Long) {
        cuckooFilter.insert(key.toString())
    }

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
        const val ENTITY_ID = "filter:entity"

        fun create(): EntityIdFilter {
            return EntityIdFilter()
        }

        @JvmStatic
        fun createId(): String {
            return ENTITY_ID
        }

        @JvmStatic
        fun createKey(): Key<EntityIdFilter> {
            return Key.create(EntityIdFilter::class.java, createId())
        }
    }
}
