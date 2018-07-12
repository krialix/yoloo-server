package com.yoloo.server.feed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class FeedApplication

fun main(args: Array<String>) {
    runApplication<FeedApplication>(*args)
}
