package com.yoloo.server.post

import com.google.api.gax.rpc.TransportChannelProvider
import com.yoloo.server.common.config.AppengineConfig
import com.yoloo.server.common.config.CacheConfig
import com.yoloo.server.common.config.IdGeneratorConfig
import com.yoloo.server.common.config.SchedulerConfig
import com.yoloo.server.post.config.LocalChannelProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.*

@Import(
    value = [
        AppengineConfig::class,
        CacheConfig::class,
        IdGeneratorConfig::class,
        SchedulerConfig::class
    ]
)
@ServletComponentScan
@SpringBootApplication
class PostApplication : SpringBootServletInitializer() {

    @Configuration
    @Profile("dev")
    @ComponentScan(lazyInit = true)
    internal class DevConfig

    @Profile("dev")
    @Bean
    fun transportChannelProvider(): TransportChannelProvider {
        return LocalChannelProvider()
    }
}

fun main(args: Array<String>) {
    runApplication<PostApplication>(*args)
}
