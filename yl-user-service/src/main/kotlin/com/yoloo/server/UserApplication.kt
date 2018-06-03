package com.yoloo.server

import com.yoloo.server.common.config.AppengineConfig
import com.yoloo.server.common.config.EtagConfig
import com.yoloo.server.common.config.IdGeneratorConfig
import com.yoloo.server.common.config.SchedulerConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

//@ServletComponentScan
@Import(
    value = [
        AppengineConfig::class,
        IdGeneratorConfig::class,
        SchedulerConfig::class,
        EtagConfig::class
    ]
)
@EnableScheduling
@SpringBootApplication
class UserApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}
