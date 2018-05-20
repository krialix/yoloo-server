package com.yoloo.server

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAdminServer
@ServletComponentScan
@EnableScheduling
@SpringBootApplication
class UserApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}
