package com.yoloo.server

import com.google.common.truth.Truth.assertThat
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.appengine.TestBase
import com.yoloo.server.common.util.NoArg
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DemoService : TestBase() {

    @BeforeEach
    internal fun setUp() {
        ofy().factory().register(Entity1::class.java)
    }

    @Test
    fun getEntity() {
        ofy().save().entity(Entity1(id = 1)).now()

        ofy().clear()

        val likeable = loadEntity(Entity1::class.java)

        assertThat(likeable).isNotNull()
    }

    private fun loadEntity(type: Class<out Likeable>) : Likeable {
        return ofy().load().key(Key.create(type, 1)).now()
    }

    @NoArg
    @Entity
    data class Entity1(@Id var id: Long) : Likeable

    interface Likeable
}