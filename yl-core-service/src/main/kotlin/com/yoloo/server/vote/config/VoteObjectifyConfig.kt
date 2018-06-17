package com.yoloo.server.vote.config

import com.yoloo.server.objectify.configuration.ObjectifyConfigurer
import com.yoloo.server.vote.entity.Vote
import org.springframework.context.annotation.Configuration

@Configuration
class VoteObjectifyConfig : ObjectifyConfigurer {

    override fun registerEntities(): List<Class<*>> {
        return listOf(Vote::class.java)
    }
}