package com.yoloo.server.group

import com.yoloo.server.common.config.AppengineConfig
import com.yoloo.server.common.config.EtagConfig
import com.yoloo.server.common.config.IdGeneratorConfig
import com.yoloo.server.common.config.SchedulerConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Import

@Import(
    value = [
        AppengineConfig::class,
        IdGeneratorConfig::class,
        SchedulerConfig::class,
        EtagConfig::class
    ]
)
@SpringBootApplication
class GroupApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<GroupApplication>(*args)
}
