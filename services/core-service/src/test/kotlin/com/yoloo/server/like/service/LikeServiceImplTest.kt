package com.yoloo.server.like.service

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.appengine.TestBase
import com.yoloo.spring.autoconfiguration.appengine.services.counter.CounterServiceImpl
import com.yoloo.server.post.entity.Post
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LikeServiceImplTest : TestBase() {

    lateinit var likeService: LikeService

    @BeforeEach
    fun setUp() {
        likeService = LikeServiceImpl(MemcacheServiceFactory.getAsyncMemcacheService(), CounterServiceImpl())

        ofy().factory().register(Post::class.java)
    }

    @Test
    fun like() {
        likeService.like(1, 2, Post::class.java)
    }
}
